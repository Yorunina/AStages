package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.ARestrictionType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;

import java.util.*;

public class ARestrictionManager {
    public static final AItemManager ITEM_INSTANCE = new AItemManager();
    public static final ADimensionManager DIMENSION_INSTANCE = new ADimensionManager();
    public static final AMobManager MOB_INSTANCE = new AMobManager();
    public static final ATimeManager TIME_INSTANCE = new ATimeManager();
    public static final AStructureManager STRUCTURE_INSTANCE = new AStructureManager();
    public static final ARecipeManager RECIPE_INSTANCE = new ARecipeManager();
    public static final AScreenManager SCREEN_INSTANCE = new AScreenManager();
    public static final AOreManager ORE_INSTANCE = new AOreManager();
    public static final APetManager PET_INSTANCE = new APetManager();

    public static final Set<String> ALL_STAGES = new HashSet<>();
    public static final Set<String> ORE_STAGES = new HashSet<>();

    static {
//        var rest = new ARecipeRestriction("id_auto");
//        rest.type = RecipeType.SMELTING;
//        rest.restrict(new ResourceLocation("minecraft", "charcoal"));
//
//        var rest1 = new ARecipeRestriction("id_auto_1");
//        rest1.type = MekanismRecipeType.CRUSHING.getRecipeType();
//        rest1.restrict(new ResourceLocation("mekanism", "crushing/gravel_to_sand"));
//
//        RECIPE_INSTANCE.addRestriction("test_auto", rest);
//        RECIPE_INSTANCE.addRestriction("test_auto_mod", rest1);

        ARecipeRestriction restriction = new ARecipeRestriction("astages/botania1");
        restriction.type = BotaniaRecipeTypes.BREW_TYPE;
        restriction.restrict(new ResourceLocation("botania", "brew/speed"));

        ARestrictionManager.RECIPE_INSTANCE.addRestriction("stage_rec", restriction);

        ARecipeRestriction restriction1 = new ARecipeRestriction("astages/botania2");
        restriction1.type = BotaniaRecipeTypes.ORECHID_TYPE;
        restriction1.restrict(new ResourceLocation("botania", "orechid/coal_ore"));
        restriction1.restrict(new ResourceLocation("botania", "orechid/iron_ore"));
        restriction1.restrict(new ResourceLocation("botania", "orechid/redstone_ore"));
        restriction1.restrict(new ResourceLocation("botania", "orechid/copper_ore"));
        restriction1.restrict(new ResourceLocation("botania", "orechid/gold_ore"));
        restriction1.restrict(new ResourceLocation("botania", "orechid/emerald_ore"));
        restriction1.restrict(new ResourceLocation("botania", "orechid/lapis_ore"));
        restriction1.restrict(new ResourceLocation("botania", "orechid/diamond_ore"));

        ARestrictionManager.RECIPE_INSTANCE.addRestriction("stage_rec", restriction1);
    }

    public static boolean isOreStage(String stage) {
        return ORE_STAGES.contains(stage);
    }

    public static boolean areOreStages(List<String> stages) {
        for (String stage : ORE_STAGES) {
            if (stages.contains(stage)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T extends ARestriction> T getRestrictionById(@NotNull ARestrictionType type, String id) {
        return switch (type) {
            case ITEM -> (T) ITEM_INSTANCE.getRestriction(id);
            case MOB -> (T) MOB_INSTANCE.getRestriction(id);
            case DIMENSION -> (T) DIMENSION_INSTANCE.getRestriction(id);
            case TIME -> (T) TIME_INSTANCE.getRestriction(id);
            case STRUCTURE -> (T) STRUCTURE_INSTANCE.getRestriction(id);
            case RECIPE -> (T) RECIPE_INSTANCE.getRestriction(id);
            case SCREEN -> (T) SCREEN_INSTANCE.getRestriction(id);
            case ORE -> (T) ORE_INSTANCE.getRestriction(id);
            case PET -> (T) PET_INSTANCE.getRestriction(id);
        };
    }
}
