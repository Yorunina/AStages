package com.alessandro.astages.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.annotation.develop.Info;
import com.alessandro.astages.api.annotation.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@NotNullParamsAndMethodsReturn
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class OfflinePlayerStage {
    public static final LevelResource ASTAGES_DATA_DIR = new LevelResource("astagesdata");

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
        checkAStagesDataFolder(event.getServer());
    }

    @Info("Migration purpose only!")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var file = getPlayerStageFile(event.getEntity());
        CompoundTag tag = readNbtFromFile(file);

        if (!tag.contains("stages")) {
            var listTag = new ListTag();
            getPlayerStagesFromCapability(event.getEntity()).forEach(stage -> listTag.add(StringTag.valueOf(stage)));

            tag.put("stages", listTag);
        }

        writeNbtToFile(tag, file);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void checkAStagesDataFolder(MinecraftServer server) {
        var dataDir = server.getWorldPath(ASTAGES_DATA_DIR).toFile();

        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public static File getAStagesDataFolder(MinecraftServer server) {
        return server.getWorldPath(ASTAGES_DATA_DIR).toFile();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File getPlayerStageFile(Player player) {
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

    public static List<String> getPlayerStagesFromCapability(Player player) {
        AtomicReference<List<String>> playerStages = new AtomicReference<>(new ArrayList<>());
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> playerStages.set(playerStage.getStages()));
        return playerStages.get();
    }

    public static CompoundTag readNbtFromFile(File file) {
        CompoundTag tag = null;
        try { tag = NbtIo.read(file); } catch (IOException ignoredException) { }
        if (tag == null) { tag = new CompoundTag(); }

        return tag;
    }

    public static void writeNbtToFile(CompoundTag tag, File file) {
        try {
            NbtIo.write(tag, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
