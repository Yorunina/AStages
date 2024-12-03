package com.alessandro.astages.core;

import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.ud.JeiRecipeSyncerS2CPacket;
import com.alessandro.astages.networking.packet.ud.OreSyncerS2CPacket;
import com.alessandro.astages.networking.packet.ud.RequestClientReloadS2CPacket;
import com.alessandro.astages.networking.packet.ud.RequestJeiClientReloadS2CPacket;
import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.ARestrictionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ARestrictionManager {
    // ADD SLOT RESTRICTION
    // ADD ENCHANTMENT RESTRICTION

    public static final AItemManager ITEM_INSTANCE = new AItemManager();
    public static final ADimensionManager DIMENSION_INSTANCE = new ADimensionManager();
    public static final AMobManager MOB_INSTANCE = new AMobManager();
//    public static final ATimeManager TIME_INSTANCE = new ATimeManager();
    public static final AStructureManager STRUCTURE_INSTANCE = new AStructureManager();
    public static final ARecipeManager RECIPE_INSTANCE = new ARecipeManager();
    public static final AScreenManager SCREEN_INSTANCE = new AScreenManager();
    public static final AOreManager ORE_INSTANCE = new AOreManager();
    public static final APetManager PET_INSTANCE = new APetManager();
    public static final AEnchantManager ENCHANT_INSTANCE = new AEnchantManager();

    public static Set<String> ALL_STAGES = new HashSet<>();
    public static Set<String> ORE_STAGES = new HashSet<>();

    public static void reloadBeforeScripts() {
        ITEM_INSTANCE.reloadBeforeScripts();
        DIMENSION_INSTANCE.reloadBeforeScripts();
        MOB_INSTANCE.reloadBeforeScripts();
        STRUCTURE_INSTANCE.reloadBeforeScripts();
        RECIPE_INSTANCE.reloadBeforeScripts();
        SCREEN_INSTANCE.reloadBeforeScripts();
        ORE_INSTANCE.reloadBeforeScripts();
        PET_INSTANCE.reloadBeforeScripts();
        ENCHANT_INSTANCE.reloadBeforeScripts();

        ALL_STAGES.clear();
        ORE_STAGES.clear();

        ModNetworking.sendToClients(new RequestClientReloadS2CPacket());
    }

    public static void reloadAfterScripts() {
        // ITEMS AUTOMATICALLY -> question/answer system
        // JEI
        ModNetworking.sendToClients(new RequestJeiClientReloadS2CPacket());

        // RECIPE
        ARestrictionManager.RECIPE_INSTANCE.getRestrictions().forEach((s, r) -> r.forEach(restriction -> ModNetworking.sendToClients(new JeiRecipeSyncerS2CPacket(restriction.id, s, restriction.type, restriction.recipes))));

        // ORE
        ARestrictionManager.ORE_INSTANCE.getRestrictions().forEach((s, r) -> r.forEach(restriction -> {
            ModNetworking.sendToClients(new OreSyncerS2CPacket(restriction.id, s, restriction.original, restriction.replacement, false));
        }));
    }

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

//        ARecipeRestriction restriction = new ARecipeRestriction("astages/botania1");
//        restriction.type = BotaniaRecipeTypes.BREW_TYPE;
//        restriction.restrict(new ResourceLocation("botania", "brew/speed"));
//
//        ARestrictionManager.RECIPE_INSTANCE.addRestriction("stage_rec", restriction);
//
//        ARecipeRestriction restriction1 = new ARecipeRestriction("astages/botania2");
//        restriction1.type = BotaniaRecipeTypes.ORECHID_TYPE;
//        restriction1.restrict(new ResourceLocation("botania", "orechid/coal_ore"));
//        restriction1.restrict(new ResourceLocation("botania", "orechid/iron_ore"));
//        restriction1.restrict(new ResourceLocation("botania", "orechid/redstone_ore"));
//        restriction1.restrict(new ResourceLocation("botania", "orechid/copper_ore"));
//        restriction1.restrict(new ResourceLocation("botania", "orechid/gold_ore"));
//        restriction1.restrict(new ResourceLocation("botania", "orechid/emerald_ore"));
//        restriction1.restrict(new ResourceLocation("botania", "orechid/lapis_ore"));
//        restriction1.restrict(new ResourceLocation("botania", "orechid/diamond_ore"));
//
//        ARestrictionManager.RECIPE_INSTANCE.addRestriction("stage_rec", restriction1);
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
    public static <T extends ARestriction> @Nullable T getRestrictionById(@NotNull ARestrictionType type, String id) {
        return switch (type) {
            case ITEM -> (T) ITEM_INSTANCE.getRestriction(id);
            case MOB -> (T) MOB_INSTANCE.getRestriction(id);
            case DIMENSION -> (T) DIMENSION_INSTANCE.getRestriction(id);
//            case TIME -> (T) TIME_INSTANCE.getRestriction(id);
            case TIME -> null;
            case STRUCTURE -> (T) STRUCTURE_INSTANCE.getRestriction(id);
            case RECIPE -> (T) RECIPE_INSTANCE.getRestriction(id);
            case SCREEN -> (T) SCREEN_INSTANCE.getRestriction(id);
            case ORE -> (T) ORE_INSTANCE.getRestriction(id);
            case PET -> (T) PET_INSTANCE.getRestriction(id);
            case ENCHANT -> (T) ENCHANT_INSTANCE.getRestriction(id);
        };
    }
}
