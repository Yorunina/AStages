package com.alessandro.astages.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AFileIOUtils;
import com.alessandro.astages.api.AStagesFolderSystem;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.event.player.*;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.stages.ClientStagesSyncerS2CPacket;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.File;
import java.util.*;

@NotNullParamsAndMethodsReturn
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class OfflinePlayerStage {
    public static final Map<UUID, List<String>> CACHE = new HashMap<>();
    public static Map<UUID, String> UUID_USERNAME;
    public static Map<String, UUID> USERNAME_UUID;

    @SubscribeEvent
    public static void serverStarted(ServerStartingEvent event) {
        UUID_USERNAME = AFileIOUtils.readMapOrDefault(getConfigFile("uuid_to_username"), UUID.class, String.class);
        USERNAME_UUID = AFileIOUtils.readMapOrDefault(getConfigFile("username_to_uuid"), String.class, UUID.class);
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppingEvent event) {
        AFileIOUtils.writeFileContent(getConfigFile("uuid_to_username"), UUID_USERNAME);
        AFileIOUtils.writeFileContent(getConfigFile("username_to_uuid"), USERNAME_UUID);
    }

    @Info("Migration purpose only!")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var playerUUID = event.getEntity().getUUID();
        var playerName = event.getEntity().getGameProfile().getName();
        UUID_USERNAME.put(playerUUID, playerName);
        USERNAME_UUID.put(playerName, playerUUID);

        var file = getPermanentStagesFile(event.getEntity());
        var stageList = AFileIOUtils.readList(file, String.class);

        if (stageList == null) {
            var oldList = getPlayerStagesFromCapability(event.getEntity());
            AFileIOUtils.writeFileContent(file, oldList);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        var player = event.getEntity();
        markAsDirty(player);
        CACHE.remove(player.getUUID()); // Clear CACHE
    }

    private static File getConfigFile(String fileName) {
        var file = new File(AStagesFolderSystem.getAStagesDataFolder(), fileName + ".json");
        return AFileIOUtils.getOrCreateFile(file);
    }

    private static File getPermanentStagesFile(Player player) {
        return getPermanentStagesFile(player.getUUID());
    }

    private static File getPermanentStagesFile(UUID uuid) {
        var file = new File(AStagesFolderSystem.getPlayerPermanentFolder(), uuid + ".json");
        return AFileIOUtils.getOrCreateFile(file);
    }

    public static File getTemporaryStagesFile(Player player) {
        return getTemporaryStagesFile(player.getUUID());
    }

    public static File getTemporaryStagesFile(UUID uuid) {
        var file = new File(AStagesFolderSystem.getPlayerTemporaryFolder(), uuid + ".json");
        return AFileIOUtils.getOrCreateFile(file);
    }

    public static List<String> getPlayerStagesFromCache(Player player) {
        return getPlayerStagesFromCache(player.getUUID());
    }

    public static List<String> getPlayerStagesFromCache(UUID uuid) {
        if (!CACHE.containsKey(uuid)) {
            var stages = AFileIOUtils.readListOrDefault(getPermanentStagesFile(uuid), String.class);
            CACHE.put(uuid, stages);
        }

        return CACHE.get(uuid);
    }

    public static void addPlayerStage(Player player, String stage) {
        addPlayerStage(player.getUUID(), stage);
    }

    public static void addPlayerStage(UUID uuid, String stage) {
        CACHE.computeIfAbsent(uuid, k -> new ArrayList<>()).add(stage);
    }

    public static void addPlayerStages(Player player, List<String> stages) {
        addPlayerStages(player.getUUID(), stages);
    }

    public static void addPlayerStages(UUID uuid, List<String> stages) {
        CACHE.computeIfAbsent(uuid, k -> new ArrayList<>()).addAll(stages);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static AStatus removePlayerStage(Player player, String stage) {
        return removePlayerStage(player.getUUID(), stage);
    }

    public static AStatus removePlayerStage(UUID uuid, String stage) {
        return CACHE.computeIfAbsent(uuid, k -> new ArrayList<>()).remove(stage) ? AStatus.SUCCESS : AStatus.NOT_PRESENT;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static AStatus removePlayerStages(Player player, List<String> stages) {
        return removePlayerStages(player.getUUID(), stages);
    }

    public static AStatus removePlayerStages(UUID uuid, List<String> stages) {
        return CACHE.computeIfAbsent(uuid, k -> new ArrayList<>()).removeAll(stages) ? AStatus.SUCCESS : AStatus.NOT_PRESENT;
    }

    @Info("Synchronization is required only if the player is 'physically' in the server!")
    public static void synchronizeWithClient(UUID uuid, AOperation operation, String stage, boolean silentTitle) {
        var player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        if (player != null) { synchronizeWithClient(player, operation, stage, silentTitle); }
    }

    public static void synchronizeWithClient(Player player, AOperation operation, String stage, boolean silentTitle) {
        synchronizeWithClient(player, operation, Collections.singletonList(stage), silentTitle);
    }

    @Info("Synchronization is required only if the player is 'physically' in the server!")
    public static void synchronizeWithClient(UUID uuid, AOperation operation, List<String> stages, boolean silentTitle) {
        var player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        if (player != null) { synchronizeWithClient(player, operation, stages, silentTitle); }
    }

    public static void synchronizeWithClient(Player player, AOperation operation, List<String> stages, boolean silentTitle) {
        AStagesUtils.checkPlayerStages(player, operation, stages);

        var event = new StageSyncedPlayerEvent(player, operation, stages);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            ANetworking.sendToPlayer(new ClientStagesSyncerS2CPacket(stages, operation), (ServerPlayer) player);

            if (!silentTitle) {
                if (player instanceof ServerPlayer serverPlayer) {
                    stages.forEach(stage -> AStagesUtil.showTitles(serverPlayer, operation, stage));
                }
            }

            switch (operation) {
                case ADD -> MinecraftForge.EVENT_BUS.post(new StageAddedPlayerEvent(player, stages.get(0)));
                case ADD_ALL -> MinecraftForge.EVENT_BUS.post(new AllStagesAddedPlayerEvent(player, stages));
                case REMOVE -> MinecraftForge.EVENT_BUS.post(new StageRemovedPlayerEvent(player, stages.get(0)));
                case REMOVE_ALL -> MinecraftForge.EVENT_BUS.post(new AllStagesRemovedPlayerEvent(player, stages));
                case LOGIN -> MinecraftForge.EVENT_BUS.post(new StageLoginPlayerEvent(player, stages));
            }
        } else {
            switch (event.getOperation()) {
                case ADD -> removePlayerStage(player, stages.get(0));
                case ADD_ALL, LOGIN -> removePlayerStages(player, stages);
                case REMOVE -> addPlayerStage(player, stages.get(0));
                case REMOVE_ALL -> addPlayerStages(player, stages);
            }
        }
    }

    // When saving, call this
    public static void markAsDirty(Player player) {
        var stages = CACHE.get(player.getUUID());
        AFileIOUtils.writeFileContent(getPermanentStagesFile(player), stages);
    }

    public static List<String> getPlayerStagesFromCapability(Player player) {
        return PlayerStageWrapper.getStages(player);
    }
}
