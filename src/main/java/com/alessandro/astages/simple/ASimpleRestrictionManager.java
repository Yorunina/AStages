package com.alessandro.astages.simple;

import com.alessandro.astages.AStages;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ASimpleRestrictionManager {
    public static Map<ASimpleRestrictionType, List<ASimpleRestriction>> RESTRICTIONS = null;
    private static int canBeReloadable = 0;

    //.setLenient()
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    public static final Type TYPE = new TypeToken<Map<ASimpleRestrictionType, List<ASimpleRestriction>>>(){}.getType();

    public static void addRestriction(ASimpleRestrictionType type, String id, String stage, String object) {
        var newList = RESTRICTIONS.getOrDefault(type, new ArrayList<>());
        var restriction = new ASimpleRestriction(id, stage, object);
        newList.add(restriction);
        RESTRICTIONS.put(type, newList);

        synchronizeWithServer(type, restriction);

        canBeReloadable++;

        if (canBeReloadable == 5) {
            writeToFile();
            canBeReloadable = 0;
        }
    }

    public static void synchronizeWithServer(ASimpleRestrictionType type, ASimpleRestriction restriction) {
        switch (type) {
            case ITEM -> ASimpleElaborator.elaborateItem(restriction);
            case MOD -> ASimpleElaborator.elaborateMod(restriction);
            case DIMENSION -> ASimpleElaborator.elaborateDimension(restriction);
            case GUI -> ASimpleElaborator.elaborateGui(restriction);
            case ORE -> ASimpleElaborator.elaborateOre(restriction);
            case STRUCTURE -> ASimpleElaborator.elaborateStructure(restriction);
            case BIOME -> AStages.LOGGER.debug("NOT YET IMPLEMENTED!");
            case TAME -> ASimpleElaborator.elaborateTame(restriction);
            case MOUNT -> ASimpleElaborator.elaborateMount(restriction);
            case RECIPE -> ASimpleElaborator.elaborateRecipe(restriction);
            case ARMOR -> ASimpleElaborator.elaborateArmor(restriction);
        }
    }

    public static void readFromFile() {
        if (RESTRICTIONS != null) { AStages.LOGGER.error("SKIPPED READING!"); }

        AStages.LOGGER.debug("READING IN PROGRESS...");

        try (var fileReader = new FileReader(getConfigFileWithRestrictions())) {
            RESTRICTIONS = GSON.fromJson(fileReader, TYPE);

            if (RESTRICTIONS == null) {
                RESTRICTIONS = new HashMap<>();
            } else {
                for (var type : RESTRICTIONS.keySet()) {
                    for (var restriction : RESTRICTIONS.get(type)) {
                        synchronizeWithServer(type, restriction);
                    }
                }
            }
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
    public static File getConfigFileWithRestrictions() {
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
