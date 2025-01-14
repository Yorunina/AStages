package com.alessandro.astages.core.stage;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AStageManager {
    public static List<AStage> STAGES = new StageArrayList<>();

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
