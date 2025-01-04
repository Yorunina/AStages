package com.alessandro.astages.mixin.ore;

import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ServerPlayerGameMode.class)
public class AServerPlayerGameMode {
//    @Unique
//    private ServerPlayerGameMode serverPlayerGameMode$self() {
//        return (ServerPlayerGameMode) (Object) this;
//    }

    @Shadow @Final protected ServerPlayer player;

    @Unique
    private @Nullable BlockState astages$getReplacer(BlockState state) {
        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(player, state);
        // AStages.LOGGER.debug(restriction.replacement.toString());
        if (restriction != null) {
            return restriction.getReplacement();
        } else {
            return null;
        }
    }

    @Redirect(method = "handleBlockBreakAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private @NotNull BlockState onBlockBreak(@NotNull ServerLevel instance, BlockPos pos) {
        var rep = astages$getReplacer(instance.getBlockState(pos));
        return rep == null ? instance.getBlockState(pos) : rep;
    }

    @Redirect(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private @NotNull BlockState onDestroyBlock(@NotNull ServerLevel instance, BlockPos pos) {
        var rep = astages$getReplacer(instance.getBlockState(pos));
        return rep == null ? instance.getBlockState(pos) : rep;
    }

    @Redirect(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private @NotNull BlockState onItemUse(@NotNull Level instance, BlockPos pos) {
        var rep = astages$getReplacer(instance.getBlockState(pos));
        return rep == null ? instance.getBlockState(pos) : rep;
    }
}
