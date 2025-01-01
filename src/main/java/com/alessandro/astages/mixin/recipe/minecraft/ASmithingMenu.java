package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.ARecipeManager;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(SmithingMenu.class)
public class ASmithingMenu {
    @Shadow @Final private Level level;
    @Unique private Player astages$player;

    @Unique
    private SmithingMenu smithingMenu$self() {
        return (SmithingMenu) (Object) this;
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    public void astages$init(int containerId, @NotNull Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
        astages$player = playerInventory.player;
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;)V", at = @At("TAIL"))
    public void astages$init(int containerId, @NotNull Inventory playerInventory, CallbackInfo ci) {
        astages$player = playerInventory.player;
    }

    @Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/SmithingRecipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void astages$createResult(CallbackInfo ci, List<SmithingRecipe> recipes, @NotNull SmithingRecipe recipe) {
        var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(astages$player, new ARecipeManager.RecipeWrapper(recipe.getType(), recipe.getId()));

        if (restriction != null) {
            smithingMenu$self().resultSlots.setItem(0, ItemStack.EMPTY);
            ci.cancel();
        }
    }
}
