package com.alessandro.astages.mixin.recipe.botania;

import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.ARecipeManager;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vazkii.botania.api.recipe.OrechidRecipe;
import vazkii.botania.common.block.flower.functional.OrechidBlockEntity;

import java.util.Iterator;
import java.util.List;

@Mixin(value = OrechidBlockEntity.class, remap = false, priority = 1001)
public class AOrechidBlockEntity {
    @Unique
    public OrechidBlockEntity astages$self() {
        return (OrechidBlockEntity) (Object) this;
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "findMatchingRecipe", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void astages$findRecipe(BlockPos coords, CallbackInfoReturnable<OrechidRecipe> cir, BlockState input, List<WeightedEntry.Wrapper<OrechidRecipe>> values, Iterator var4, OrechidRecipe recipe) {
        if (ARestrictionManager.RECIPE_INSTANCE.isRestrictionListEmpty()) { return; }
        if (astages$self().getLevel() == null) { return; }
        if (astages$self().getLevel().getServer() == null) { return; }

        astages$self().getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
            Player player = AStagesUtil.getPlayerFromUUID(astages$self().getLevel().getServer(), blockStage.getOwner());
            var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new ARecipeManager.RecipeWrapper(recipe.getType(), recipe.getId()));

            if (restriction != null) {
                cir.cancel();
            }
        });
    }
}
