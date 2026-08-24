package com.alessandro.astages.infrastructure.mixin.ore;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.ARestrictionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class)
public abstract class APlayer {
    @Shadow public abstract boolean hasCorrectToolForDrops(BlockState pState);

    @Unique
    private Player self$player() {
        return (Player) (Object) this;
    }

    @Inject(method = "hasCorrectToolForDrops", at = @At("RETURN"), cancellable = true)
    public void astages$hasCorrectToolForDrops(BlockState original, CallbackInfoReturnable<Boolean> cir) {
        var self = self$player();
        if (self instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), original);
            if (restriction != null) {
                cir.setReturnValue(hasCorrectToolForDrops(restriction.getReplacement()));
            }
        } else {
            var restriction = AClientRestrictionManager.ORE_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), original);
            if (restriction != null) {
                cir.setReturnValue(hasCorrectToolForDrops(restriction.getReplacement()));
            }
        }
    }
}
