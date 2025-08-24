package com.alessandro.astages.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.event.custom.actions.*;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@NotNullParamsAndMethodsReturn
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class OfflinePlayerStage {
    @UnderDevelopment public static final Map<UUID, List<String>> CACHE = new HashMap<>();

    public static final LevelResource ASTAGES_DATA_DIR = new LevelResource("astagesdata");
    public static final String STAGE_KEY = "stages";

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        checkAStagesDataFolder(event.getServer());
    }

    @Info("Migration purpose only!")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var file = getPlayerStageFile(event.getEntity());
        CompoundTag tag = readNbtFromFile(file);

        if (!tag.contains(STAGE_KEY)) {
            var listTag = new ListTag();
            getPlayerStagesFromCapability(event.getEntity()).forEach(stage -> listTag.add(StringTag.valueOf(stage)));

            tag.put(STAGE_KEY, listTag);
        }

        writeNbtToFile(tag, file);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void checkAStagesDataFolder(MinecraftServer server) {
        var dataDir = server.getWorldPath(ASTAGES_DATA_DIR).toFile();

        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    private static File getAStagesDataFolder(MinecraftServer server) {
        return server.getWorldPath(ASTAGES_DATA_DIR).toFile();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File getPlayerStageFile(Player player) {
        var file = new File(getAStagesDataFolder(Objects.requireNonNull(player.getServer())), player.getStringUUID() + ".dat");

        if (!file.exists()) {
            try {
                // Create file only if DOESN'T exist
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }

    public static List<String> getPlayerStagesFromFile(Player player) {
        if (!CACHE.containsKey(player.getUUID())) {
            var file = getPlayerStageFile(player);
            var nbt = readNbtFromFile(file);
            var stages = new ArrayList<String>();

            var listTag = (ListTag) nbt.get(STAGE_KEY);
            if (listTag != null) {
                listTag.forEach(tag -> {
                    if (tag instanceof StringTag stringTag) {
                        stages.add(stringTag.getAsString());
                    }
                });
            }

            CACHE.put(player.getUUID(), stages);
        }

        return CACHE.get(player.getUUID());
    }

    public static void addPlayerStage(Player player, String stage) {
        CACHE.computeIfAbsent(player.getUUID(), k -> new ArrayList<>()).add(stage);
    }

    public static void addPlayerStages(Player player, List<String> stages) {
        CACHE.computeIfAbsent(player.getUUID(), k -> new ArrayList<>()).addAll(stages);
    }

    public static void removePlayerStage(Player player, String stage) {
        CACHE.computeIfAbsent(player.getUUID(), k -> new ArrayList<>()).remove(stage);
    }

    public static void removePlayerStages(Player player, List<String> stages) {
        CACHE.computeIfAbsent(player.getUUID(), k -> new ArrayList<>()).removeAll(stages);
    }

    public static void synchronizeWithClient(Player player, AOperation operation, String stage, boolean silentTitle) {
        synchronizeWithClient(player, operation, Collections.singletonList(stage), silentTitle);
    }

    public static void synchronizeWithClient(Player player, AOperation operation, List<String> stages, boolean silentTitle) {
        AStagesUtils.checkStages(player, operation, stages);

        var event = new StageSyncedPlayerEvent(player, operation, stages);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            ANetworking.sendToPlayer(new StageDataSyncS2CPacket(stages, operation), (ServerPlayer) player);

            if (!silentTitle) {
                if (player instanceof ServerPlayer serverPlayer) {
                    stages.forEach(stage -> AStagesUtil.showTitles(serverPlayer, operation, stage));
                }
            }

            switch (operation) {
                case ADD -> MinecraftForge.EVENT_BUS.post(new StageAddedPlayerEvent(player, stages.get(0)));
                case ADD_ALL -> MinecraftForge.EVENT_BUS.post(new AllStageAddedPlayerEvent(player, stages));
                case REMOVE -> MinecraftForge.EVENT_BUS.post(new StageRemovedPlayerEvent(player, stages.get(0)));
                case REMOVE_ALL -> MinecraftForge.EVENT_BUS.post(new AllStageRemovedPlayerEvent(player, stages));
                // case GET -> MinecraftForge.EVENT_BUS.post(new StageGetPlayerEvent(player, stages));
                case LOGIN -> MinecraftForge.EVENT_BUS.post(new StageLoginPlayerEvent(player, stages));
            }
        } else {
            switch (event.getOperation()) {
                case ADD -> removePlayerStage(player, stages.get(0));
                case ADD_ALL, LOGIN -> removePlayerStages(player, stages);
                case REMOVE -> addPlayerStage(player, stages.get(0));
                case REMOVE_ALL -> addPlayerStages(player, stages);
                // case GET -> AStages.LOGGER.info("Get operation cannot be cancelled!");
            }
        }
    }

    public static void markAsDirty(Player player) {

    }

    public static List<String> getPlayerStagesFromCapability(Player player) {
        AtomicReference<List<String>> playerStages = new AtomicReference<>(new ArrayList<>());
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> playerStages.set(playerStage.getStages()));
        return playerStages.get();
    }

    private static CompoundTag readNbtFromFile(File file) {
        CompoundTag tag = null;
        try { tag = NbtIo.read(file); } catch (IOException ignoredException) { }
        if (tag == null) { tag = new CompoundTag(); }

        return tag;
    }

    private static void writeNbtToFile(CompoundTag tag, File file) {
        try {
            NbtIo.write(tag, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
