package com.alessandro.astages.mixin;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import mcjty.incontrol.compat.ModRuleCompatibilityLayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModRuleCompatibilityLayer.class, remap = false)
public class AModRuleCompatibilityLayer {
    @Inject(method = "hasGameStages", at = @At("RETURN"), cancellable = true)
    public void hasGameStages(@NotNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "hasGameStage", at = @At("RETURN"), cancellable = true)
    public void hasGameStage(@NotNull Player player, String stage, CallbackInfoReturnable<Boolean> cir) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> cir.setReturnValue(playerStage.getStages().contains(stage)));
    }

    @Inject(method = "addGameStage", at = @At("HEAD"), cancellable = true)
    public void addGameStage(@NotNull Player player, String stage, CallbackInfo ci) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.addStage(stage);
            playerStage.setChangedFor(player, PlayerStage.Operation.ADD, stage);
            ci.cancel();
        });
    }

    @Inject(method = "addGameStage", at = @At("HEAD"), cancellable = true)
    public void removeGameStage(@NotNull Player player, String stage, CallbackInfo ci) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.removeStage(stage);
            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE, stage);
            ci.cancel();
        });
    }
}
