package com.alessandro.astages.core.stage;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public class AStageManager {
    private static final Map<String, AStage> STAGES = new HashMap<>();

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

        STAGES.values().forEach(stage -> {
            if (!stage.isServerOnly()) {
                ARestrictionManager.ALL_STAGES.add(stage.getStage());
            }
        });

//        ARestrictionManager.reflectAllStagesChangesToClients(null, ARestrictionManager.ALL_STAGES, SyncOperation.ADD);

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
