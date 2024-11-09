package com.alessandro.astages.core.stage;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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

    static {
        STAGES.add(new AStage("stage_test_class").setAddTitle(Component.literal("Added CUSTOM")).setAddSubTitle(Component.literal("CUSTOM").withStyle(ChatFormatting.GREEN)));
    }
}
