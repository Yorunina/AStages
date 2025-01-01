package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.core.ARecipeManager;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(CraftingMenu.class)
public class ACraftingMenu {
    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Ljava/util/Optional;get()Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void astages$slotChanged(AbstractContainerMenu pMenu, Level pLevel, Player pPlayer, CraftingContainer pContainer, ResultContainer pResult, CallbackInfo ci, ServerPlayer serverPlayer, ItemStack $$6, @NotNull Optional<CraftingRecipe> optional) {
        if (optional.isPresent()) {
            var recipe = optional.get();
            var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(serverPlayer, new ARecipeManager.RecipeWrapper(recipe.getType(), recipe.getId()));

            if (restriction != null) {
                ci.cancel();
            }
        }
    }
}
