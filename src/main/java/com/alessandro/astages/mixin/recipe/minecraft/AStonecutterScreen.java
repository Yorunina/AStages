package com.alessandro.astages.mixin.recipe.minecraft;

import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StonecutterScreen.class)
public class AStonecutterScreen {
//    @Inject(method = "renderRecipes", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void astages$renderRecipes(GuiGraphics pGuiGraphics, int pX, int pY, int pStartIndex, CallbackInfo ci, List<StonecutterRecipe> recipes, int $$5, int $$6, int $$7, int $$8, int $$9) {
//        AStages.LOGGER.debug("RENDER!");
//        if (recipes != null) {
//            AStages.LOGGER.debug(recipes.toString());
//        }
//    }
}
