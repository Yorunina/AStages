package com.alessandro.astages.infrastructure.mixin.integration.biomancy;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.github.elenterius.biomancy.crafting.recipe.BioForgingRecipe;
import com.github.elenterius.biomancy.menu.BioForgeMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@NotNullParams
@Mixin(value = BioForgeMenu.class, remap = false)
public class ABioForgeMenu {
    @Shadow(remap = false)
    @Final
    ResultContainer resultContainer;

    @Inject(method = "updateResultSlot", at = @At("HEAD"), cancellable = true, remap = false)
    private void astages$updateResultSlot(ServerPlayer serverPlayer, CallbackInfo ci) {
        BioForgeMenu self = (BioForgeMenu) (Object) this;
        BioForgingRecipe recipe = self.getSelectedRecipe();
        if (recipe == null) return;

        var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(
            AHolder.serverAndPlayer(serverPlayer),
            new RecipeWrapper(recipe.getType(), recipe.getId())
        );

        if (restriction != null) {
            // Clear the result slot and sync to client before cancelling the vanilla logic.
            resultContainer.setItem(0, ItemStack.EMPTY);
            self.setRemoteSlot(0, ItemStack.EMPTY);
            self.broadcastChanges();
            ci.cancel();
        }
    }
}
