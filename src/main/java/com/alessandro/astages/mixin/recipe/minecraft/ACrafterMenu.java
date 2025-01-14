package com.alessandro.astages.mixin.recipe.minecraft;

import net.minecraft.world.inventory.CrafterMenu;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CrafterMenu.class)
public class ACrafterMenu {
//    @Inject(method = "refreshRecipeResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
//    private void astages$refreshRecipeResult(CallbackInfo ci, @Local Level level, @Local CraftingInput craftinginput) {
//        ItemStack newStack = CrafterBlock.getPotentialResults(level, craftinginput).map((recipeHolder) -> {
//            var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction()
//
//            recipeHolder.value().assemble(craftinginput, level.registryAccess());
//        }).orElse(ItemStack.EMPTY);
//    }
}
