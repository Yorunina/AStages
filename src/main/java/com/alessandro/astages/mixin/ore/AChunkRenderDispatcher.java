package com.alessandro.astages.mixin.ore;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkRenderDispatcher.RenderChunk.RebuildTask.class)
public class AChunkRenderDispatcher {
    @ModifyExpressionValue(method = "compile", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState astages$getBlockState(BlockState original) {
        return AClientRestrictionManager.ORE_INSTANCE.getReplacement(original);
    }
}
