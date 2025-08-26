package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@NotNullParams
@Mixin(value = AbstractFurnaceBlockEntity.class)
public class AAbstractFurnaceBlockEntity {
    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;getMaxStackSize()I"), cancellable = true)
    private static void astages$serverTick(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci, @Nullable @Local Recipe<?> recipe) {
        if (level.getServer() == null) { return; }

        AtomicReference<UUID> atomicOwner = new AtomicReference<>();
        blockEntity.getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> atomicOwner.set(blockStage.getOwner()));

        UUID blockOwner = atomicOwner.get();
        Player player = AStagesUtil.getPlayerFromUUID(level.getServer(), blockOwner);
        if (player == null || recipe == null) { return; }

        var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new RecipeWrapper(recipe.getType(), recipe.getId()));

        if (restriction != null) {
            ci.cancel();

            level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, false), Block.UPDATE_ALL);
        } else {
            level.setBlock(pos, state.setValue(AbstractFurnaceBlock.LIT, true), Block.UPDATE_ALL);
        }
    }
}
