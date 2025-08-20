package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.util.annotations.NotNullParams;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@NotNullParams
@Mixin(SmithingMenu.class)
public class ASmithingMenu {
    @Unique private Player astages$player;

    @Unique
    private SmithingMenu smithingMenu$self() {
        return (SmithingMenu) (Object) this;
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    public void astages$init(int containerId, Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
        astages$player = playerInventory.player;
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;)V", at = @At("TAIL"))
    public void astages$init(int containerId, Inventory playerInventory, CallbackInfo ci) {
        astages$player = playerInventory.player;
    }

    @Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/SmithingRecipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    public void astages$createResult(CallbackInfo ci, @Local SmithingRecipe recipe) {
        var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(new RecipeWrapper(recipe.getType(), recipe.getId()), astages$player, astages$player.getServer());

        if (restriction != null) {
            smithingMenu$self().resultSlots.setItem(0, ItemStack.EMPTY);
            ci.cancel();
        }
    }
}
