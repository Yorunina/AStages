package com.alessandro.astages.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.event.player.*;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.ClientStagesSyncerS2CPacket;
import com.alessandro.astages.util.AStagesUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@NotNullParamsAndMethodsReturn
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class OfflinePlayerStage {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public static final Type TYPE_UUID_USERNAME = new TypeToken<Map<UUID, String>>(){}.getType();
    public static final Type TYPE_USERNAME_UUID = new TypeToken<Map<String, UUID>>(){}.getType();

    public static final Map<UUID, List<String>> CACHE = new HashMap<>();
    public static Map<UUID, String> UUID_USERNAME;
    public static Map<String, UUID> USERNAME_UUID;

    public static final LevelResource ASTAGES_DATA_DIR = new LevelResource("astagesdata");
    public static final String STAGE_KEY = "stages";

    @SubscribeEvent
    public static void serverStarted(ServerStartingEvent event) {
        var server = event.getServer();
        checkAStagesDataFolder(server);

        try (var fileReader = new FileReader(getConfigFile("uuid_to_username", server))) {
            UUID_USERNAME = GSON.fromJson(fileReader, TYPE_UUID_USERNAME);

            if (UUID_USERNAME == null) {
                UUID_USERNAME = new HashMap<>();
            }
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }

        try (var fileReader = new FileReader(getConfigFile("username_to_uuid", server))) {
            USERNAME_UUID = GSON.fromJson(fileReader, TYPE_USERNAME_UUID);

            if (USERNAME_UUID == null) {
                USERNAME_UUID = new HashMap<>();
            }
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppingEvent event) {
        var server = event.getServer();
        try (var fileWriter = new FileWriter(getConfigFile("uuid_to_username", server))) {
            GSON.toJson(UUID_USERNAME, fileWriter);
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }

        try (var fileWriter = new FileWriter(getConfigFile("username_to_uuid", server))) {
            GSON.toJson(USERNAME_UUID, fileWriter);
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    @Info("Migration purpose only!")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var playerUUID = event.getEntity().getUUID();
        var playerName = event.getEntity().getGameProfile().getName();
        UUID_USERNAME.put(playerUUID, playerName);
        USERNAME_UUID.put(playerName, playerUUID);

        var file = getPlayerStageFile(event.getEntity());
        CompoundTag tag = readNbtFromFile(file);

        if (!tag.contains(STAGE_KEY)) {
            var listTag = new ListTag();
            getPlayerStagesFromCapability(event.getEntity()).forEach(stage -> listTag.add(StringTag.valueOf(stage)));

            tag.put(STAGE_KEY, listTag);

            writeNbtToFile(tag, file);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        var player = event.getEntity();
        markAsDirty(player);
        CACHE.remove(player.getUUID()); // Clear CACHE
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
    private static File getConfigFile(String fileName, MinecraftServer server) {
        var file = new File(getAStagesDataFolder(server), fileName + ".json");

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

    private static File getPlayerStageFile(Player player) {
        return getPlayerStageFile(player.getServer(), player.getUUID());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File getPlayerStageFile(@Nullable MinecraftServer server, UUID uuid) {
        var file = new File(getAStagesDataFolder(Objects.requireNonNull(server)), uuid + ".dat");

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

    public static List<String> getPlayerStagesFromCache(Player player) {
        return getPlayerStagesFromCache(player.getServer(), player.getUUID());
    }

    public static List<String> getPlayerStagesFromCache(UUID uuid) {
        return getPlayerStagesFromCache(ServerLifecycleHooks.getCurrentServer(), uuid);
    }

    public static List<String> getPlayerStagesFromCache(@Nullable MinecraftServer server, UUID uuid) {
        if (!CACHE.containsKey(uuid)) {
            var file = getPlayerStageFile(server, uuid);
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

    public static AStatus removePlayerStage(Player player, String stage) {
        return removePlayerStage(player.getUUID(), stage);
    }

    public static AStatus removePlayerStage(UUID uuid, String stage) {
        return CACHE.computeIfAbsent(uuid, k -> new ArrayList<>()).remove(stage) ? AStatus.SUCCESS : AStatus.NOT_PRESENT;
    }

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
        var tag = new CompoundTag();

        var listTag = new ListTag();
        stages.forEach(stage -> listTag.add(StringTag.valueOf(stage)));
        tag.put(STAGE_KEY, listTag);

        writeNbtToFile(tag, getPlayerStageFile(player));
    }

    public static List<String> getPlayerStagesFromCapability(Player player) {
        return PlayerStageWrapper.getStages(player);
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
