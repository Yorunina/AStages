package com.alessandro.astages.config;

import com.alessandro.astages.AStages;
import net.minecraft.ChatFormatting;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class AStagesCommon {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_TITLE_AFTER_STAGE_ADDING;
    public static final ForgeConfigSpec.EnumValue<ChatFormatting> TITLE_COLOR;

    static {
        BUILDER.push("Configs for AStages Mod");

        ENABLE_TITLE_AFTER_STAGE_ADDING = BUILDER.comment("Enable or disable if mod show a title for each stage add to a player")
            .define("Enable Titles", true);

        TITLE_COLOR = BUILDER.comment("If previous setting is enabled, set the color of the title that appears")
            .defineEnum("Title color", ChatFormatting.RED);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
