package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@NotNullParams
@Mixin(CampfireBlock.class)
public class ACampfireBlock {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void astages$use(BlockState pState, Level level, BlockPos pPos, Player player, InteractionHand pHand, BlockHitResult pHit, CallbackInfoReturnable<InteractionResult> cir, BlockEntity blockentity, CampfireBlockEntity campfireblockentity, ItemStack itemstack, Optional<CampfireCookingRecipe> optional) {
        if (optional.isPresent()) {
            var recipe = optional.get();
            var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(new RecipeWrapper(recipe.getType(), recipe.getId()), player, level.getServer());

            if (restriction != null) {
                cir.setReturnValue(InteractionResult.CONSUME);
            }
        }
    }
}
