package com.alessandro.astages.infrastructure.mixin;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.tom.storagemod.gui.CraftingTerminalMenu;
import com.tom.storagemod.platform.PlatformRecipe;
import com.tom.storagemod.tile.CraftingTerminalBlockEntity;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

@Mixin(CraftingTerminalBlockEntity.class)
public class ACraftingTerminalBlockEntity {
    @Shadow
    private HashSet<CraftingTerminalMenu> craftingListeners;

    @Shadow
    protected void onCraftingMatrixChanged() {}

    @Redirect(method = "onCraftingMatrixChanged", at = @At(value = "INVOKE", target = "Lcom/tom/storagemod/platform/PlatformRecipe;assemble(Lnet/minecraft/world/Container;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;"), remap = false)
    private ItemStack astages$checkRecipe(PlatformRecipe recipe, Container craftMatrix, RegistryAccess registryAccess) {
        Recipe<?> mcRecipe = recipe.recipe();
        for (CraftingTerminalMenu menu : craftingListeners) {
            Player player = ((AStorageTerminalMenuAccessor) menu).astages$getPinv().player;
            if (player instanceof ServerPlayer serverPlayer) {
                var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(
                    AHolder.serverAndPlayer(serverPlayer),
                    new RecipeWrapper(mcRecipe.getType(), mcRecipe.getId())
                );

                if (restriction != null) {
                    return ItemStack.EMPTY;
                }
            }
        }

        return recipe.assemble(craftMatrix, registryAccess);
    }

    @Inject(method = "registerCrafting", at = @At("TAIL"), remap = false)
    private void astages$onRegisterCrafting(CraftingTerminalMenu menu, CallbackInfo ci) {
        Level level = ((CraftingTerminalBlockEntity) (Object) this).getLevel();
        if (level != null && !level.isClientSide) {
            onCraftingMatrixChanged();
        }
    }
}
