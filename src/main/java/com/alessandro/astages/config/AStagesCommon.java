package com.alessandro.astages.config;

import net.minecraft.ChatFormatting;
import net.minecraftforge.common.ForgeConfigSpec;

public class AStagesCommon {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_TITLE_AFTER_STAGE_ADDING;
    public static final ForgeConfigSpec.EnumValue<ChatFormatting> TITLE_COLOR;
    public static final ForgeConfigSpec.ConfigValue<Integer> TICK_STRUCTURE_UPDATING;

    static {
        BUILDER.push("Configs for AStages Mod");

        ENABLE_TITLE_AFTER_STAGE_ADDING = BUILDER.comment("Enable or disable if mod show a title for each stage add to a player")
            .define("Enable Titles", true);

        TITLE_COLOR = BUILDER.comment("If previous setting is enabled, set the color of the title that appears")
            .defineEnum("Title color", ChatFormatting.RED);

        TICK_STRUCTURE_UPDATING = BUILDER.comment("Every how many ticks the updating of the structures in which the player is located is required")
            .define("Tick Structure Updating", 1);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
