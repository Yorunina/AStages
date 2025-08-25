package com.alessandro.astages.mixin.ore.event;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.core.ARestrictionManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockEvent.BreakEvent.class)
public class ABreakEvent {
    @ModifyVariable(method = "<init>", at = @At(value = "HEAD"), argsOnly = true)
    private static BlockState astages$init(BlockState original, @Local(argsOnly = true) Player player) {
        return ARestrictionManager.ORE_INSTANCE.getReplacementForPlayerActions(AHolder.serverAndPlayer(player), original);
    }
}
