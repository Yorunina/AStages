package com.alessandro.astages.infrastructure.mixin.integration.biomancy;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeRestriction;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

// BioForgeScreenController is package-private in com.github.elenterius.biomancy.client.gui,
// so we must reference it by string target instead of importing the class.
//
// Note: We bypass AClientRestrictionManager.RECIPE_INSTANCE.getRestriction() because
// AClientRecipeEvaluator.evaluateCache has a bug where it returns early (null) when
// no ARecipeModRestriction exists, skipping normal ARecipeRestriction checks.
// We iterate the client registry directly instead.
@Mixin(targets = "com.github.elenterius.biomancy.client.gui.BioForgeScreenController", remap = false)
public class ABioForgeScreenController {

    @ModifyArg(
        method = "updateAndSearchRecipes",
        at = @At(value = "INVOKE", target = "Lcom/github/elenterius/biomancy/client/gui/BioForgeScreenController;setShownRecipes(Ljava/util/List;)V", remap = false),
        index = 0,
        remap = false
    )
    private List<RecipeCollection> astages$filterShownRecipes(List<RecipeCollection> recipes) {
        // Hide any RecipeCollection that contains a recipe restricted for the current client player.
        // Each Bio-Forge RecipeCollection contains exactly one BioForgingRecipe.
        return recipes.stream().filter(this::astages$isAllowed).toList();
    }

    private boolean astages$isAllowed(RecipeCollection collection) {
        var holder = AClientHolder.serverAndPlayer();

        for (Recipe<?> recipe : collection.getRecipes()) {
            var wrapper = new RecipeWrapper(recipe.getType(), recipe.getId());

            // Bypass the buggy evaluator: iterate the client registry directly.
            for (AClientRecipeRestriction restriction : AClientRestrictionManager.RECIPE_INSTANCE.getRegistry().getRecipeRestrictions()) {
                if (!restriction.isRestricted(wrapper)) { continue; }

                // If the player lacks the stage (on either server or player scope), the recipe is hidden.
                boolean hasServerStage = AStagesClientUtils.hasStage(holder, AStageType.SERVER, restriction.getStage());
                boolean hasPlayerStage = AStagesClientUtils.hasStage(holder, AStageType.PLAYER, restriction.getStage());

                if (!hasServerStage && !hasPlayerStage) {
                    return false;
                }
            }
        }
        return true;
    }
}
