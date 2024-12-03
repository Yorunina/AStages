package com.alessandro.astages.simple;

import com.alessandro.astages.AStages;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class ASimpleRestrictionManager {
    public static Map<ASimpleRestrictionType, List<ASimpleRestriction>> RESTRICTIONS = null;
    private static int canBeReloadable = 0;

    //.setLenient()
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public static final Type TYPE = new TypeToken<Map<ASimpleRestrictionType, List<ASimpleRestriction>>>(){}.getType();

    public static void addRestriction(ASimpleRestrictionType type, String id, String stage, String object) {
        var newList = RESTRICTIONS.getOrDefault(type, new ArrayList<>());
        newList.add(new ASimpleRestriction(id, stage, object));
        RESTRICTIONS.put(type, newList);

        synchronizeWithServer();

        canBeReloadable++;

        if (canBeReloadable == 5) {
            writeToFile();
            canBeReloadable = 0;
        }
    }

    public static void synchronizeWithServer() {
        for (var entry : RESTRICTIONS.entrySet()) {
            switch (entry.getKey()) {
                case ITEM -> entry.getValue().forEach(ASimpleElaborator::elaborateItem);
                case MOD -> entry.getValue().forEach(ASimpleElaborator::elaborateMod);
                case DIMENSION -> entry.getValue().forEach(ASimpleElaborator::elaborateDimension);
                case GUI -> entry.getValue().forEach(ASimpleElaborator::elaborateGui);
                case ORE -> entry.getValue().forEach(ASimpleElaborator::elaborateOre);
                case STRUCTURE, BIOME -> entry.getValue().forEach(simple -> AStages.LOGGER.debug("NOT YET IMPLEMENTED!"));
                case TAME -> entry.getValue().forEach(ASimpleElaborator::elaborateTame);
                case MOUNT -> entry.getValue().forEach(ASimpleElaborator::elaborateMount);
                case RECIPE -> entry.getValue().forEach(ASimpleElaborator::elaborateRecipe);
                case ARMOR -> entry.getValue().forEach(ASimpleElaborator::elaborateArmor);
            }
        }
    }

    public static void readFromFile() {
        if (RESTRICTIONS != null) { AStages.LOGGER.error("SKIPPED READING!"); }

        AStages.LOGGER.debug("READING IN PROGRESS...");

        try (var fileReader = new FileReader(getConfigFileWithRestrictions())) {
//            if (RESTRICTIONS == null) {
//                RESTRICTIONS = new HashMap<>();
//            } else {
//                synchronizeWithServer();
//                return;
//            }

            RESTRICTIONS = GSON.fromJson(fileReader, TYPE);
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    public static void writeToFile() {
        AStages.LOGGER.debug("WRITING IN PROGRESS...");

        try (var fileWriter = new FileWriter(getConfigFileWithRestrictions())) {
            GSON.toJson(RESTRICTIONS, fileWriter);
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static @NotNull File getConfigFileWithRestrictions() {
        File saveDir = new File(FMLPaths.CONFIGDIR.get().toFile(), "astages");

        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        File file = new File(saveDir, "simple_restrictions.json");
        try {
            file.createNewFile();
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }

        return file;
    }
}
