package com.alessandro.astages.core.stage;

import com.alessandro.astages.AStages;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AStageManager {
    public static List<AStage> STAGES = new StageArrayList<>();

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

        AStages.LOGGER.debug("NOT YET IMPLEMENTED!");
    }

    @Contract(pure = true)
    public static @Nullable AStage getStage(String stage) {
        for (var s : STAGES) {
            if (s.stage.equals(stage)) {
                return s;
            }
        }

        return null;
    }
}
