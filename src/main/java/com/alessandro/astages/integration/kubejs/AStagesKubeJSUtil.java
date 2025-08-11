package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.capability.AProvider;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.*;
import com.alessandro.astages.core.server.restriction.item.AItemModRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemTagRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.core.stage.AStage;
import com.alessandro.astages.core.stage.AStageManager;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.ACompareCondition;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AStagesKubeJSUtil {
    // Player Stages
    public static void addStageToPlayer(String stage, Player player) {
        var playerStage = getPlayerData(player);
        playerStage.addStage(stage);
        playerStage.setChangedFor(player, PlayerStage.Operation.ADD, stage);
    }

    public static void removeStageFromPlayer(String stage, Player player) {
        var playerStage = getPlayerData(player);
        playerStage.removeStage(stage);
        playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE, stage);
    }

    public static List<String> getStagesFromPlayer(Player player) {
        var playerStage = getPlayerData(player);
        return new ArrayList<>(playerStage.getStages());
    }

    public static void removeAllStagesFromPlayer(Player player) {
        var playerStage = getPlayerData(player);
        playerStage.removeAllStages();
        playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE_ALL, null);
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

    public static PlayerStage getPlayerData(Player player) {
        return player.getData(AProvider.PLAYER_STAGE);
    }

    // Server Stages
    public static void addStageToServer(String stage, MinecraftServer server) {
        ServerStageData.getData(server).add(stage);
    }

    public static void removeStageFromServer(String stage, MinecraftServer server) {
        ServerStageData.getData(server).remove(stage);
    }

    public static void removeAllStagesFromServer(MinecraftServer server) {
        ServerStageData.getData(server).removeAll();
    }

    public static boolean serverHasStage(String stage, MinecraftServer server) {
        return ServerStageData.getData(server).has(stage);
    }

    public static boolean serverHasAtLeastOneStage(List<String> stages, MinecraftServer server) {
        for (var stage : stages) {
            if (serverHasStage(stage, server)) {
                return true;
            }
        }

        return false;
    }

    public static boolean serverHasAllStages(List<String> stages, MinecraftServer server) {
        for (var stage : stages) {
            if (!serverHasStage(stage, server)) {
                return false;
            }
        }

        return true;
    }

    public static ServerStageData getServerData(MinecraftServer server) {
        return ServerStageData.getData(server);
    }

    // General
    public static <T> @Nullable T getRestrictionById(ARestrictionType type, String id) {
        return ARestrictionManager.getRestrictionById(id, type);
    }

    // STAGES
    public static AStage customizeStage(String s) {
        var stage = new AStage(s);

        AStageManager.addStage(stage);

        return stage;
    }

    // ITEM Restrictions
    public static AItemRestriction addRestrictionForItem(String id, String stage, Item... items) {
        var restriction = new AItemRestriction(id, stage);
        for (var item : items) {
            restriction.restrict(item);
        }
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemPredicateRestriction addRestrictionForPredicate(String id, String stage, ResourceLocation modelId) {
        var restriction = new AItemPredicateRestriction(id, stage);
        restriction.restrict(modelId);
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemModRestriction addRestrictionForMod(String id, String stage, String modId) {
        var restriction = new AItemModRestriction(id, stage);
        restriction.restrict(modId);
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemTagRestriction addRestrictionForTag(String id, String stage, ResourceLocation name) {
        var restriction = new AItemTagRestriction(id, stage);
        restriction.restrict(name);
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemRestriction addRestrictionForArmor(String id, String stage, Item... armors) {
        var restriction = new AItemRestriction(id, stage);
        for (var armor : armors) {
            restriction.restrict(armor);
        }

        restriction.setArmorAttributes();

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

    // RECIPE Restrictions
    public static ARecipeRestriction addRestrictionForRecipe(String id, String stage, RecipeType<?> recipeType, ResourceLocation... recipeIds) {
        var restriction = new ARecipeRestriction(id, stage);

        for (ResourceLocation r : recipeIds) {
            restriction.restrict(new RecipeWrapper(recipeType, r));
        }

        ARestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARecipeModRestriction addRestrictionForModRecipe(String id, String stage, String modId) {
        var restriction = new ARecipeModRestriction(id, stage);
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

    // REGION Restrictions
    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, BlockPos center, int deltaX, int deltaY, int deltaZ) {
        var restriction = new ARegionRestriction(id, stage);
        restriction.setArea(type, center, deltaX, deltaY, deltaZ);

        ARestrictionManager.REGION_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        var restriction = new ARegionRestriction(id, stage);
        restriction.setArea(type, minX, minY, minZ, maxX, maxY, maxZ);

        ARestrictionManager.REGION_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, BlockPos center, int radius) {
        var restriction = new ARegionRestriction(id, stage);
        restriction.setArea(type, center, radius);

        ARestrictionManager.REGION_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, BlockPos from, BlockPos to) {
        var restriction = new ARegionRestriction(id, stage);
        restriction.setArea(type, from, to);

        ARestrictionManager.REGION_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    // LOOT Restrictions
    public static ALootRestriction addRestrictionForLoot(String id, String stage) {
        var restriction = new ALootRestriction(id, stage);

        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return restriction;
    }
}
