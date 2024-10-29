package com.alessandro.astages.mixin.recipe.botania;

import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.ARecipeManager;
import com.alessandro.astages.core.ARecipeRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.BotanicalBreweryRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BreweryBlockEntity;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(value = BreweryBlockEntity.class, remap = false, priority = 1001)
public class ABreweryBlockEntity {
    @Shadow public BotanicalBreweryRecipe recipe;

    @Unique
    public BreweryBlockEntity astages$self() {
        return (BreweryBlockEntity) (Object) this;
    }

    static {
        ARecipeRestriction restriction = new ARecipeRestriction("astages/botania1");
        restriction.restrict(new ResourceLocation("botania", "brew/speed"));

        ARestrictionManager.RECIPE_INSTANCE.addRestriction("stage_rec", restriction);
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "findRecipe", at = @At("HEAD"), cancellable = true)
    public void astages$findRecipe(CallbackInfo ci) {
        if (ARestrictionManager.RECIPE_INSTANCE.isRestrictionListEmpty()) { return; }
        if (astages$self().getLevel() == null) { return; }
        if (astages$self().getLevel().getServer() == null) { return; }

        Optional<BotanicalBreweryRecipe> rec = astages$self().getLevel().getRecipeManager().getRecipeFor(BotaniaRecipeTypes.BREW_TYPE, astages$self().getItemHandler(), astages$self().getLevel());

        if (rec.isPresent()) {
            AtomicBoolean toReturn = new AtomicBoolean(true);

            astages$self().getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
                Player player = AStagesUtil.getPlayerFromUUID(astages$self().getLevel().getServer(), blockStage.getOwner());
                var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new ARecipeManager.RecipeWrapper(rec.get().getType(), rec.get().getId()));

                if (restriction != null) {
                    toReturn.set(false);
                }
            });


            if (toReturn.get()) {
                this.recipe = rec.get();
                astages$self().getLevel().setBlockAndUpdate(astages$self().getBlockPos(), BotaniaBlocks.brewery.defaultBlockState().setValue(BlockStateProperties.POWERED, true));
            }
        }
    }
}
