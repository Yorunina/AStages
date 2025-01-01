package com.alessandro.astages.mixin.ore;

import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class APlayer {
    @Shadow public abstract boolean hasCorrectToolForDrops(BlockState pState);

    @Unique
    private Player player$self() {
        return (Player) (Object) this;
    }

    @Inject(method = "hasCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
    public void hasCorrectToolForDrop(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(player$self(), state);

        if (restriction != null) {
            cir.setReturnValue(hasCorrectToolForDrops(restriction.replacement));
        }
    }
}
