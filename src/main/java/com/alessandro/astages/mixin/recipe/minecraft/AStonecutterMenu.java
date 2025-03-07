package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.util.develop.UnderDevelopment;
import net.minecraft.world.inventory.StonecutterMenu;
import org.spongepowered.asm.mixin.Mixin;

@UnderDevelopment
@Mixin(StonecutterMenu.class)
public class AStonecutterMenu {
//    @Shadow private List<StonecutterRecipe> recipes;
//    @Shadow @Final private Level level;
//    @Unique private Player astages$player;
//
//    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
//    public void astages$init(int containerId, @NotNull Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
//        astages$player = playerInventory.player;
//    }
//
//    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;)V", at = @At("TAIL"))
//    public void astages$init(int containerId, @NotNull Inventory playerInventory, CallbackInfo ci) {
//        astages$player = playerInventory.player;
//    }
//
//    @Inject(method = "setupRecipeList", at = @At("TAIL"), cancellable = true)
//    public void astages$setupRecipeList(Container container, @NotNull ItemStack stack, @NotNull CallbackInfo ci) {
////        if (!stack.isEmpty()) {
//
//        AStages.LOGGER.debug("Called new SETUP!");
//        recipes = level.getRecipeManager().getRecipesFor(RecipeType.STONECUTTING, container, level)
//            .stream()
//            .filter(recipe -> {
//                var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(astages$player, new ARecipeManager.RecipeWrapper(recipe.getType(), recipe.getId()));
//
//                return restriction == null;
//            })
//            .collect(Collectors.toList());
//
//        AStages.LOGGER.debug(recipes.toString());
//        ci.cancel();
////        }
//    }
}
