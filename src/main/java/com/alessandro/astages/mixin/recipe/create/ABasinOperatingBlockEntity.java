package com.alessandro.astages.mixin.recipe.create;

import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.ARecipeManager;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.block.block_entity.BreweryBlockEntity;

import java.util.List;

@Mixin(value = BasinOperatingBlockEntity.class, remap = false)
public class ABasinOperatingBlockEntity {

    @Unique
    public BasinOperatingBlockEntity astages$self() {
        return (BasinOperatingBlockEntity) (Object) this;
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getMatchingRecipes", at = @At("RETURN"))
    private void astages$getMatchingRecipes(@NotNull CallbackInfoReturnable<List<Recipe<?>>> cir) {
        if (ARestrictionManager.RECIPE_INSTANCE.isRestrictionListEmpty()) { return; }
        if (astages$self().getLevel() == null) { return; }
        if (astages$self().getLevel().getServer() == null) { return; }

        List<Recipe<?>> recipes = cir.getReturnValue();

        astages$self().getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
            Player player = AStagesUtil.getPlayerFromUUID(astages$self().getLevel().getServer(), blockStage.getOwner());

            for (var recipe : recipes) {
                var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new ARecipeManager.RecipeWrapper(recipe.getType(), recipe.getId()));

                if (restriction != null) {
                    recipes.remove(recipe);
                }
            }
        });
    }
}
