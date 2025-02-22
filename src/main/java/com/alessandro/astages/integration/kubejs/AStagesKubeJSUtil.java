package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.*;
import com.alessandro.astages.core.restriction.item.AItemModRestriction;
import com.alessandro.astages.core.restriction.item.AItemRestriction;
import com.alessandro.astages.core.restriction.item.AItemTagRestriction;
import com.alessandro.astages.core.stage.AStage;
import com.alessandro.astages.core.stage.AStageManager;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.ACompareCondition;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.develop.Info;
import com.alessandro.astages.util.develop.UnderDevelopment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AStagesKubeJSUtil {
    // Player Stages
    public static void addStageToPlayer(String stage, Player player) {
        getPlayerCapability(player).ifPresent(playerStage -> {
            playerStage.addStage(stage);
            playerStage.setChangedFor(player, PlayerStage.Operation.ADD, stage);
        });
    }

    public static void removeStageFromPlayer(String stage, Player player) {
        getPlayerCapability(player).ifPresent(playerStage -> {
            playerStage.removeStage(stage);
            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE, stage);
        });
    }

    public static List<String> getStagesFromPlayer(Player player) {
        List<String> toReturn = new ArrayList<>();
        getPlayerCapability(player).ifPresent(playerStage -> toReturn.addAll(playerStage.getStages()));
        return toReturn;
    }

    public static void removeAllStagesFromPlayer(Player player) {
        getPlayerCapability(player).ifPresent(playerStage -> {
            playerStage.removeAllStages();
            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE_ALL, null);
        });
    }

    public static boolean playerHasStage(String stage, Player player) {
        return AStagesUtil.hasStage(player, stage);
    }

    public static boolean playerHasAtLeastOneStage(List<String> stages, Player player) {
        for (var stage : stages) {
            if (playerHasStage(stage, player)) {
                return true;
            }
        }

        return false;
    }

    public static boolean playerHasAllStages(List<String> stages, Player player) {
        for (var stage : stages) {
            if (!playerHasStage(stage, player)) {
                return false;
            }
        }

        return true;
    }

    public static @NotNull LazyOptional<PlayerStage> getPlayerCapability(Player player) {
        return player.getCapability(PlayerStageProvider.PLAYER_STAGE);
    }

    public static <T> @Nullable T getRestrictionById(ARestrictionType type, String id) {
        return ARestrictionManager.getRestrictionById(type, id);
    }

    // STAGES
    public static AStage customizeStage(String s) {
        var stage = new AStage(s);

        AStageManager.STAGES.add(stage);

        return stage;
    }

    // ITEM Restrictions
    public static AItemRestriction addRestrictionForItem(String id, String stage, Item... items) {
        // var restriction = new AItemRestriction(id, RestrictionType.RUNTIME);
//        var restriction = new AItemRestriction(id, stage);
//
//        for (var item : items) {
//            restriction.restrict(itemStack -> itemStack.is(item));
//        }
//
//        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        var restriction = new AItemRestriction(id, stage);
        for (var item : items) {
            restriction.restrict(item);
        }
        ARestrictionManager.NEW_ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    @UnderDevelopment
    public static AItemRestriction addRestrictionForPredicate(String id, String stage, Predicate<ItemStack> predicate) {
        // var restriction = new AItemRestriction(id, RestrictionType.RUNTIME);
//        var restriction = new AItemRestriction(id, stage);
//        restriction.restrict(predicate);
//
//        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return null;
    }

    public static AItemModRestriction addRestrictionForMod(String id, String stage, String modId) {
//        var restriction = new AItemRestriction(id, stage);
//        restriction.restrict(itemStack -> modId.equalsIgnoreCase(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).getNamespace()));
//
//        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        var restriction = new AItemModRestriction(id, stage);
        restriction.restrict(modId);
        ARestrictionManager.NEW_ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemModRestriction addRestrictionForMod(String id, String stage, String modId, Item... ignored) {
//        var restriction = new AItemRestriction(id, stage);
//
//        restriction.restrict(itemStack ->  {
//            for (var i : ignored) {
//                if (itemStack.is(i)) { return false; }
//            }
//
//            return modId.equalsIgnoreCase(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).getNamespace());
//        });
//
//        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        var restriction = new AItemModRestriction(id, stage);
        restriction.restrict(modId);
        restriction.ignoreItems(ignored);
        ARestrictionManager.NEW_ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemTagRestriction addRestrictionForTag(String id, String stage, ResourceLocation name) {
//        var tag = ItemTags.create(name);
//        var restriction = new AItemRestriction(id, stage);
//        restriction.restrict(itemStack -> itemStack.is(tag));
//
//        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        var restriction = new AItemTagRestriction(id, stage);
        restriction.restrict(name);
        ARestrictionManager.NEW_ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemTagRestriction addRestrictionForTagAndBlacklistItems(String id, String stage, ResourceLocation name, Item... ignored) {
//        var tag = ItemTags.create(name);
//        var restriction = new AItemRestriction(id, stage);
//
//        restriction.restrict(itemStack -> {
//            for (var i : ignored) {
//                if (itemStack.is(i)) { return false; }
//            }
//
//            return itemStack.is(tag);
//        });
//
//        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        var restriction = new AItemTagRestriction(id, stage);
        restriction.restrict(name);
        restriction.ignoreItems(ignored);
        ARestrictionManager.NEW_ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    @Info("Doesn't make any sense! ")
    public static AItemRestriction addRestrictionForTag(String id, String stage, ResourceLocation name, ResourceLocation... ignored) {
//        var tag = ItemTags.create(name);
//        var restriction = new AItemRestriction(id, stage);
//
//        restriction.restrict(itemStack -> {
//            for (var i : ignored) {
//                var t = ItemTags.create(i);
//                if (itemStack.is(t)) { return false; }
//            }
//
//            return itemStack.is(tag);
//        });
//
//        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return null;
    }

    public static AItemRestriction addRestrictionForArmor(String id, String stage, Item... armors) {
//        var restriction = new AItemRestriction(id, stage);
//
//        for (var armor : armors) {
//            restriction.restrict(itemStack -> itemStack.is(armor));
//        }

        var restriction = new AItemRestriction(id, stage);
        for (var armor : armors) {
            restriction.restrict(armor);
        }

        restriction.set(Attributes.RENDERING_NAME, true)
            .set(Attributes.HIDING_TOOLTIP, false)
            .set(Attributes.PICKING_UP, true)
            .set(Attributes.STORING_IN_INVENTORY, true)
            .set(Attributes.ATTACKING, true)
            .set(Attributes.HIDING_JEI, false)
            .set(Attributes.BLOCK_PLACING, true)
            .set(Attributes.LEFT_CLICK_INTERACTIONS, true)
            .set(Attributes.RIGHT_CLICK_INTERACTIONS, true)
            .set(Attributes.BLOCK_BREAKING, true);

//        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        ARestrictionManager.NEW_ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // DIMENSION Restrictions
    public static ADimensionRestriction addRestrictionForDimension(String id, String stage, ResourceLocation dimension) {
        var restriction = new ADimensionRestriction(id, stage);
        restriction.restrict(dimension);

        ARestrictionManager.DIMENSION_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // MOB Restrictions
    public static AMobRestriction addRestrictionForMob(String id, String stage, EntityType<?> mob) {
        var restriction = new AMobRestriction(id, stage);
        restriction.restrict(mob);

        ARestrictionManager.MOB_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // RECIPE Restrictions
    public static ARecipeRestriction addRestrictionForRecipe(String id, String stage, RecipeType<?> recipeType, ResourceLocation... recipeIds) {
        var restriction = new ARecipeRestriction(id, stage);

        for (ResourceLocation r : recipeIds) {
            restriction.restrict(new RecipeWrapper(recipeType, r));
        }

        ARestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARecipeRestriction addRestrictionForModRecipe(String id, String stage, String modId) {
        var restriction = new ARecipeRestriction(id, stage);
        restriction.restrict(new RecipeModWrapper(modId));

        ARestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // SCREEN Restrictions
    public static AScreenRestriction addRestrictionForScreen(String id, String stage, MenuType<?> menu) {
        var restriction = new AScreenRestriction(id, stage);
        restriction.restrict(menu);

        ARestrictionManager.SCREEN_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // ORE Restrictions
    public static AOreRestriction addRestrictionForOre(String id, String stage, BlockState original, BlockState replacement) {
        var restriction = new AOreRestriction(id, stage);
        restriction.restrict(new OreWrapper(original, replacement));

        ARestrictionManager.ORE_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // PET Restrictions
    public static APetRestriction addRestrictionForPet(String id, String stage, EntityType<?> pet) {
        var restriction = new APetRestriction(id, stage);
        restriction.restrict(pet);

        ARestrictionManager.PET_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // STRUCTURE Restrictions
    public static AStructureRestriction addRestrictionForStructure(String id, String stage, ResourceLocation... structures) {
        var restriction = new AStructureRestriction(id, stage);

        for (var structure : structures) {
            restriction.restrict(structure);
        }

        ARestrictionManager.STRUCTURE_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // ENCHANT Restrictions
    public static AEnchantRestriction addRestrictionForEnchant(String id, String stage, Enchantment enchantment) {
        var restriction = new AEnchantRestriction(id, stage);
        restriction.restrict(enchantment);

        ARestrictionManager.ENCHANT_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AEnchantRestriction addRestrictionForEnchant(String id, String stage, Enchantment enchantment, ACompareCondition compareCondition, int level) {
        var restriction = new AEnchantRestriction(id, stage);
        restriction.restrict(enchantment)
            .set(Attributes.COMPARE_CONDITION, compareCondition)
            .set(Attributes.LEVEL, level);

        ARestrictionManager.ENCHANT_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // CROP Restrictions
    public static ACropRestriction addRestrictionForCrop(String id, String stage, Block crop) {
        var restriction = new ACropRestriction(id, stage);
        restriction.restrict(crop);

        ARestrictionManager.CROP_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ACropRestriction addRestrictionForCrop(String id, String stage, Block crop, ACompareCondition compareCondition, int age) {
        var restriction = new ACropRestriction(id, stage);
        restriction.restrict(crop)
            .set(Attributes.COMPARE_CONDITION, compareCondition)
            .set(Attributes.AGE, age);

        ARestrictionManager.CROP_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // EFFECT Restrictions
    public static AEffectRestriction addRestrictionForEffect(String id, String stage, MobEffect effect) {
        var restriction = new AEffectRestriction(id, stage);
        restriction.restrict(effect);

        ARestrictionManager.EFFECT_INSTANCE.addRestriction(restriction);

        return restriction;
    }
}
