package com.alessandro.astages.infrastructure.mixin.recipe.minecraft;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.ARestrictionManager;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@NotNullParams
@Mixin(StonecutterMenu.class)
public class AStonecutterMenu {
    @Shadow private List<StonecutterRecipe> recipes;

    @Unique private Player astages$player = null;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    public void astages$init(int containerId, Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
        astages$player = playerInventory.player;
    }

    @Inject(method = "slotsChanged", at = @At("RETURN"))
    public void astages$slotsChanged(Container inventory, CallbackInfo ci) {
        var iterator = recipes.listIterator();

        if (!astages$player.level().isClientSide()) {
            while (iterator.hasNext()) {
                var recipe = iterator.next();
                var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(AHolder.serverAndPlayer(astages$player), new RecipeWrapper(recipe.getType(), recipe.getId()));

                if (restriction != null) {
                    iterator.remove();
                }
            }
        } else {
            while (iterator.hasNext()) {
                var recipe = iterator.next();
                var restriction = AClientRestrictionManager.RECIPE_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), new RecipeWrapper(recipe.getType(), recipe.getId()));

                if (restriction != null) {
                    iterator.remove();
                }
            }
        }
    }
}
