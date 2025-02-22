package com.alessandro.astages.mixin;

import dev.shadowsoffire.fastbench.util.FastBenchUtil;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FastBenchUtil.class)
public class AFastBenchUtil {
    @Inject(method = "findRecipe", at = @At("RETURN"), remap = false)
    private static void astages$findRecipe(CraftingContainer inv, Level world, CallbackInfoReturnable<Recipe<CraftingContainer>> cir) {
//        var recipe = cir.getReturnValue();
//        var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(inv.)
    }

//    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Ldev/shadowsoffire/fastbench/util/FastBenchUtil;findRecipe(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/item/crafting/Recipe;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
//    private static void astages$findRecipe(Level world, Player player, CraftingInventoryExt inv, ResultContainer result, CallbackInfo ci, ItemStack itemstack, Recipe<CraftingContainer> oldRecipe, @NotNull Recipe<CraftingContainer> recipe) {
//        var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new RecipeWrapper(recipe.getType(), recipe.getId()));
//
//        if (restriction != null) {
//            recipe =
//        }
//    }

//    @ModifyVariable(method = "slotChangedCraftingGrid", at = @At(value = "FIELD", target = ""))
//    private static void astages$findRecipe(Level world, Player player, CraftingInventoryExt inv, ResultContainer result, CallbackInfo ci, ItemStack itemstack, Recipe<CraftingContainer> oldRecipe, @NotNull Recipe<CraftingContainer> recipe) {
//        var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new RecipeWrapper(recipe.getType(), recipe.getId()));
//
//        if (restriction != null) {
//            recipe =
//        }
//    }
}
