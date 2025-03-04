package com.alessandro.astages.core.stage;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AStageManager {
    public static final List<AStage> STAGES = new StageArrayList<>();

    public static void reloadBeforeScripts() {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            return;
        }

        STAGES.clear();
    }

    public static void reloadAfterScripts() {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            return;
        }

        STAGES.forEach(stage -> {
            if (!stage.isServerOnly()) {
                ARestrictionManager.ALL_STAGES.add(stage.getStage());
            }
        });

        AStages.LOGGER.debug("NOT YET IMPLEMENTED!");
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

    @Contract(pure = true)
    public static @Nullable AStage getStage(String stage) {
        for (var s : STAGES) {
            if (s.getStage().equals(stage)) {
                return s;
            }
        }

        return null;
    }

    public static List<AStage> getStages() {
        return STAGES;
    }
}
