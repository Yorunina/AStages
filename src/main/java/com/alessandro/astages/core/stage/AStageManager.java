package com.alessandro.astages.core.stage;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.annotations.NotNullParams;
import com.alessandro.astages.util.annotations.Nullable;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

@NotNullParams
public class AStageManager {
    private static final Map<String, AStage> STAGES = new HashMap<>();

    public static void reloadBeforeScripts() {
        STAGES.clear();
    }

    public static void reloadAfterScripts() {
        STAGES.values().forEach(stage -> {
            if (!stage.isServerOnly()) {
                ARestrictionManager.ALL_STAGES.add(stage.getStage());
            }
        });
    }

    public static boolean isServerOnly(String stage) {
        var aStage = getStage(stage);
        if (aStage != null) { return aStage.isServerOnly(); }

        return false;
    }

    public static boolean isPlayerOnly(String stage) {
        var aStage = getStage(stage);
        if (aStage != null) { return aStage.isPlayerOnly(); }

        return false;
    }

    public static void addStage(AStage stage) {
        if (STAGES.containsKey(stage.getStage())) {
            AStages.LOGGER.warn("Trying to modify stage {} twice! Operation not allowed!", stage.getStage());
            return;
        }

        STAGES.put(stage.getStage(), stage);
    }

    @Contract(pure = true)
    public static @Nullable AStage getStage(String stage) {
        return STAGES.getOrDefault(stage, null);
    }
}
