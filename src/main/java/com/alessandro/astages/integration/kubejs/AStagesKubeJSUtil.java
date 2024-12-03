package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.core.*;
import com.alessandro.astages.core.stage.AStage;
import com.alessandro.astages.core.stage.AStageManager;
import com.alessandro.astages.util.ACompareCondition;
import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.ARestrictionType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class AStagesKubeJSUtil {
    // Player Stages
    public static void addStageForPlayer(String stage, @NotNull Player player) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.addStage(stage);
            playerStage.setChangedFor(player, PlayerStage.Operation.ADD, stage);
        });
    }

    public static void removeStageForPlayer(String stage, @NotNull Player player) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.removeStage(stage);
            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE, stage);
        });
    }

    public static void removeAllStagesForPlayer(@NotNull Player player) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.removeAllStages();
            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE_ALL, null);
        });
    }

    public static <T extends ARestriction> T getRestrictionById(ARestrictionType type, String id) {
        return ARestrictionManager.getRestrictionById(type, id);
    }

    // STAGES
    public static @NotNull AStage customizeStage(String s) {
        var stage = new AStage(s);

        AStageManager.STAGES.add(stage);

        return stage;
    }

    // ITEM Restrictions
    public static @NotNull AItemRestriction addRestrictionForItem(String id, String stage, Item @NotNull ... items) {
        // var restriction = new AItemRestriction(id, RestrictionType.RUNTIME);
        var restriction = new AItemRestriction(id, stage);

        for (var item : items) {
            restriction.restrict(itemStack -> itemStack.is(item));
        }

        ARestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    public static @NotNull AItemRestriction addRestrictionForPredicate(String id, String stage, Predicate<ItemStack> predicate) {
        // var restriction = new AItemRestriction(id, RestrictionType.RUNTIME);
        var restriction = new AItemRestriction(id, stage);
        restriction.restrict(predicate);

        ARestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    public static @NotNull AItemRestriction addRestrictionForMod(String id, String stage, String modId) {
        var restriction = new AItemRestriction(id, stage);
        restriction.restrict(itemStack -> modId.equalsIgnoreCase(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).getNamespace()));

        ARestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    public static @NotNull AItemRestriction addRestrictionForMod(String id, String stage, String modId, List<Item> ignored) {
        var restriction = new AItemRestriction(id, stage);
        restriction.restrict(itemStack -> modId.equalsIgnoreCase(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).getNamespace()) && !ignored.contains(itemStack.getItem()));

        ARestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    public static @NotNull AItemRestriction addRestrictionForTag(String id, String stage, ResourceLocation name) {
        var tag = ItemTags.create(name);
        var restriction = new AItemRestriction(id, stage);
        restriction.restrict(itemStack -> itemStack.is(tag));

        ARestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    public static @NotNull AItemRestriction addRestrictionForArmor(String id, String stage, Item armor) {
        var restriction = new AItemRestriction(id, stage);
        restriction.restrict(stack -> stack.is(armor));
        restriction.setRenderItemName(true)
            .setHideTooltip(false)
            .setCanPickedUp(true)
            .setCanBeStoredInInventory(true)
            .setCanAttack(true)
            .setHideInJEI(false)
            .setCanBePlaced(true)
            .setCanItemBeUsed(true)
            .setCanBeDig(true);

        ARestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    // DIMENSION Restrictions
    public static @NotNull ADimensionRestriction addRestrictionForDimension(String id, String stage, ResourceLocation dimension) {
        var restriction = new ADimensionRestriction(id);
        restriction.restrict(dimension);

        ARestrictionManager.DIMENSION_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    // MOB Restrictions
    public static @NotNull AMobRestriction addRestrictionForMob(String id, String stage, EntityType<?> mob) {
        var restriction = new AMobRestriction(id);
        restriction.restrict(mob);

        ARestrictionManager.MOB_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    // RECIPE Restrictions
    public static @NotNull ARecipeRestriction addRestrictionForRecipe(String id, String stage, RecipeType<?> recipeType, ResourceLocation @NotNull ... recipeIds) {
        var restriction = new ARecipeRestriction(id, stage);
        restriction.type = recipeType;
        for (ResourceLocation r : recipeIds) {
            restriction.restrict(r);
        }

        ARestrictionManager.RECIPE_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    // SCREEN Restrictions
    public static @NotNull AScreenRestriction addRestrictionForScreen(String id, String stage, MenuType<?> menu) {
        var restriction = new AScreenRestriction(id);
        restriction.restrict(menu);

        ARestrictionManager.SCREEN_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    // ORE Restrictions
    public static @NotNull AOreRestriction addRestrictionForOre(String id, String stage, BlockState original, BlockState replacement) {
        var restriction = new AOreRestriction(id, stage);
        restriction.restrict(original, replacement);

        ARestrictionManager.ORE_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    // PET Restrictions
    public static @NotNull APetRestriction addRestrictionForPet(String id, String stage, EntityType<?> pet) {
        var restriction = new APetRestriction(id);
        restriction.restrict(pet);

        ARestrictionManager.PET_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    // STRUCTURE Restrictions
    public static @NotNull AStructureRestriction addRestrictionForStructure(String id, String stage, ResourceLocation structure) {
        var restriction = new AStructureRestriction(id);
        restriction.restrict(structure);

        ARestrictionManager.STRUCTURE_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    // ENCHANT Restrictions
    public static @NotNull AEnchantRestriction addRestrictionForEnchant(String id, String stage, Enchantment enchantment, ACompareCondition compareCondition, int level) {
        var restriction = new AEnchantRestriction(id);
        restriction.restrict(enchantment, compareCondition, level);

        ARestrictionManager.ENCHANT_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }
}
