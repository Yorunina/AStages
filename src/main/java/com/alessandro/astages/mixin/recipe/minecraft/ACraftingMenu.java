package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(CraftingMenu.class)
public class ACraftingMenu {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;"), cancellable = true)
    private static void astages$slotChanged(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftSlots, ResultContainer resultSlots, RecipeHolder<CraftingRecipe> crafting, CallbackInfo ci, @Local @NotNull Optional<RecipeHolder<CraftingRecipe>> optional) {
        if (optional.isPresent()) {
            var recipeHolder = optional.get();
            var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new RecipeWrapper(recipeHolder.value().getType(), recipeHolder.id()));

            if (restriction != null) {
                ci.cancel();
            }
        }
    }
}
