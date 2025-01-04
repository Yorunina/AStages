package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.*;
import com.alessandro.astages.core.stage.AStage;
import com.alessandro.astages.core.stage.AStageManager;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.ACompareCondition;
import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.ARestrictionType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AStagesKubeJSUtil {
    // Player Stages
    public static void addStageForPlayer(String stage, Player player) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.addStage(stage);
            playerStage.setChangedFor(player, PlayerStage.Operation.ADD, stage);
        });
    }

    public static void removeStageForPlayer(String stage, Player player) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.removeStage(stage);
            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE, stage);
        });
    }

    public static void removeAllStagesForPlayer(Player player) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.removeAllStages();
            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE_ALL, null);
        });
    }

    public static <T extends ARestriction> @Nullable T getRestrictionById(ARestrictionType type, String id) {
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
        var restriction = new AItemRestriction(id, stage);

        for (var item : items) {
            restriction.restrict(itemStack -> itemStack.is(item));
        }

        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemRestriction addRestrictionForPredicate(String id, String stage, Predicate<ItemStack> predicate) {
        // var restriction = new AItemRestriction(id, RestrictionType.RUNTIME);
        var restriction = new AItemRestriction(id, stage);
        restriction.restrict(predicate);

        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemRestriction addRestrictionForMod(String id, String stage, String modId) {
        var restriction = new AItemRestriction(id, stage);
        restriction.restrict(itemStack -> modId.equalsIgnoreCase(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).getNamespace()));

        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemRestriction addRestrictionForMod(String id, String stage, String modId, Item... ignored) {
        var restriction = new AItemRestriction(id, stage);

        restriction.restrict(itemStack ->  {
            for (var i : ignored) {
                if (itemStack.is(i)) { return false; }
            }

            return modId.equalsIgnoreCase(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).getNamespace());
        });

        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemRestriction addRestrictionForTag(String id, String stage, ResourceLocation name) {
        var tag = ItemTags.create(name);
        var restriction = new AItemRestriction(id, stage);
        restriction.restrict(itemStack -> itemStack.is(tag));

        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemRestriction addRestrictionForArmor(String id, String stage, Item armor) {
        var restriction = new AItemRestriction(id, stage);
        restriction.restrict(stack -> stack.is(armor));

        restriction.setAttribute(Attributes.RENDERING_NAME, true)
            .setAttribute(Attributes.HIDING_TOOLTIP, false)
            .setAttribute(Attributes.STORING_IN_INVENTORY, true)
            .setAttribute(Attributes.STORING_IN_INVENTORY, true)
            .setAttribute(Attributes.ATTACKING, true)
            .setAttribute(Attributes.HIDING_JEI, false)
            .setAttribute(Attributes.BLOCK_PLACING, true)
            .setAttribute(Attributes.LEFT_CLICK_INTERACTIONS, true)
            .setAttribute(Attributes.RIGHT_CLICK_INTERACTIONS, true)
            .setAttribute(Attributes.BLOCK_BREAKING, true);

//        restriction.setRenderItemName(true)
//            .setHideTooltip(false)
//            .setCanPickedUp(true)
//            .setCanBeStoredInInventory(true)
//            .setCanAttack(true)
//            .setHideInJEI(false)
//            .setCanBePlaced(true)
////            .setCanItemBeUsed(true)
//            .setCanItemBeLeftClicked(true)
//            .setCanItemBeRightClicked(true)
//            .setCanBeDig(true);

        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

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

//    public static @NotNull AMobRestriction addRestrictionForMob(String id, String stage, EntityType<?> mob) {
//        var tag
//        var restriction = new AMobRestriction(id);
//        restriction.restrict(mob);
//
//        ARestrictionManager.MOB_INSTANCE.addRestriction(stage, restriction);
//
//        return restriction;
//    }

    // RECIPE Restrictions
    public static ARecipeRestriction addRestrictionForRecipe(String id, String stage, RecipeType<?> recipeType, ResourceLocation @NotNull ... recipeIds) {
        var restriction = new ARecipeRestriction(id, stage);

        for (ResourceLocation r : recipeIds) {
            restriction.restrict(new RecipeWrapper(recipeType, r));
        }
//        restriction.type = recipeType;
//        for (ResourceLocation r : recipeIds) {
//            restriction.restrict(r);
//        }

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
    public static AStructureRestriction addRestrictionForStructure(String id, String stage, ResourceLocation structure) {
        var restriction = new AStructureRestriction(id, stage);
        restriction.restrict(structure);

        ARestrictionManager.STRUCTURE_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // ENCHANT Restrictions
    public static AEnchantRestriction addRestrictionForEnchant(String id, String stage, Enchantment enchantment, ACompareCondition compareCondition, int level) {
        var restriction = new AEnchantRestriction(id, stage);
        // restriction.restrict(enchantment, compareCondition, level);
        restriction.restrict(enchantment)
            .setAttribute(Attributes.COMPARE_CONDITION, compareCondition)
            .setAttribute(Attributes.LEVEL, level);

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
            .setAttribute(Attributes.COMPARE_CONDITION, compareCondition)
            .setAttribute(Attributes.AGE, age);
//            .setCompareCondition(compareCondition)
//            .setAge(age);

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
