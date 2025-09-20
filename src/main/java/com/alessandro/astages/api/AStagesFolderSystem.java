package com.alessandro.astages.api;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;

@NotNullParamsAndMethodsReturn
public class AStagesFolderSystem {
    public static final LevelResource ASTAGES_DATA_DIR = new LevelResource("astagesdata");
    public static final LevelResource SERVER_DATA_DIR = new LevelResource("server");
    public static final LevelResource PLAYER_DATA_DIR = new LevelResource("player");
    public static final LevelResource PERMANENT_STAGES_DIR = new LevelResource("permanent");
    public static final LevelResource TEMPORARY_STAGES_DIR = new LevelResource("temporary");

    private static File astagesDataFolder;
    private static File serverDataFolder;
    private static File playerDataFolder;
    private static File serverPermanentFolder;
    private static File playerPermanentFolder;
    private static File serverTemporaryFolder;
    private static File playerTemporaryFolder;

    public static void buildPaths(MinecraftServer server) {
        astagesDataFolder = server.getWorldPath(ASTAGES_DATA_DIR).toFile();

        serverDataFolder = new File(astagesDataFolder, SERVER_DATA_DIR.getId());
        playerDataFolder = new File(astagesDataFolder, PLAYER_DATA_DIR.getId());

        serverPermanentFolder = new File(serverDataFolder, PERMANENT_STAGES_DIR.getId());
        playerPermanentFolder = new File(playerDataFolder, PERMANENT_STAGES_DIR.getId());

        serverTemporaryFolder = new File(serverDataFolder, TEMPORARY_STAGES_DIR.getId());
        playerTemporaryFolder = new File(playerDataFolder, TEMPORARY_STAGES_DIR.getId());
    }

    public static void createDirectories() {
        AFileIOUtils.createDirectory(astagesDataFolder);

        AFileIOUtils.createDirectory(serverDataFolder);
        AFileIOUtils.createDirectory(playerDataFolder);

        AFileIOUtils.createDirectory(serverPermanentFolder);
        AFileIOUtils.createDirectory(playerPermanentFolder);

        AFileIOUtils.createDirectory(serverTemporaryFolder);
        AFileIOUtils.createDirectory(playerTemporaryFolder);
    }

    public static File getAStagesDataFolder() {
        return astagesDataFolder;
    }

    public static File getServerDataFolder() {
        return serverDataFolder;
    }

    public static File getPlayerDataFolder() {
        return playerDataFolder;
    }

    public static File getServerPermanentFolder() {
        return serverPermanentFolder;
    }

    public static File getPlayerPermanentFolder() {
        return playerPermanentFolder;
    }

    public static File getServerTemporaryFolder() {
        return serverTemporaryFolder;
    }

    public static File getPlayerTemporaryFolder() {
        return playerTemporaryFolder;
    }
}
