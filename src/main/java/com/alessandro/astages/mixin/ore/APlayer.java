package com.alessandro.astages.mixin.ore;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.AInventorySlotListener;
import com.alessandro.astages.util.IPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class APlayer implements IPlayer {
    @Shadow public abstract boolean hasCorrectToolForDrops(BlockState pState);

    @Unique
    private Player player$self() {
        return (Player) (Object) this;
    }

    @Override
    public AInventorySlotListener astages$getInventoryListener() {
        return null;
    }

    @Inject(method = "hasCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
    public void hasCorrectToolForDrop(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        // var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(state);
        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(player$self(), state);

        if (restriction != null) {
            cir.setReturnValue(hasCorrectToolForDrops(restriction.replacement));
        }
//        else {
//            cir.setReturnValue(hasCorrectToolForDrops(restriction.replacement));
//        }
    }
}
