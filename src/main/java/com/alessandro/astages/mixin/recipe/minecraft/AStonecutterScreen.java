package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.AStages;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

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
