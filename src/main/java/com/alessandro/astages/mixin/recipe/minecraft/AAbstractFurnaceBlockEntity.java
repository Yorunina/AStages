package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.capability.AProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.ARecipeRestriction;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = AbstractFurnaceBlockEntity.class)
public class AAbstractFurnaceBlockEntity {
    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;getMaxStackSize()I"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void astages$serverTick(@NotNull Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci, boolean flag, boolean flag1, ItemStack itemstack, ItemStack itemstack1, boolean flag2, boolean flag3, RecipeHolder recipeholder) {
        if (level.getServer() == null) { return; }


        var data = blockEntity.getData(AProvider.BLOCK_STAGE);
        var blockOwner = data.getOwner();
        Player player = AStagesUtil.getPlayerFromUUID(level.getServer(), blockOwner);
        if (player == null || recipeholder == null) { return; }

        ARecipeRestriction restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new RecipeWrapper(recipeholder.value().getType(), recipeholder.id()));

        if (restriction != null) {
            ci.cancel();
        }
    }
}
