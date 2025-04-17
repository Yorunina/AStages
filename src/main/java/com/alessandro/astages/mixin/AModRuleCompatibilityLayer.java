package com.alessandro.astages.mixin;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import mcjty.incontrol.compat.ModRuleCompatibilityLayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = ModRuleCompatibilityLayer.class, remap = false)
public class AModRuleCompatibilityLayer {
    /**
     * @author Alessandro
     * @reason AStages integration
     */
    @Overwrite
    public boolean hasGameStages() {
        return true;
    }

    /**
     * @author Alessandro
     * @reason AStages integration
     */
    @Overwrite
    public boolean hasGameStage(@NotNull Player player, String stage) {
        var cap = player.getCapability(PlayerStageProvider.PLAYER_STAGE);

        if (cap.isPresent()) {
            var playerStage = cap.resolve();

            if (playerStage.isPresent()) {
                return playerStage.get().getStages().contains(stage);
            }
        }

        return false;
    }

    /**
     * @author Alessandro
     * @reason AStages integration
     */
    @Overwrite
    public void addGameStage(@NotNull Player player, String stage) {
        var cap = player.getCapability(PlayerStageProvider.PLAYER_STAGE);

        if (cap.isPresent()) {
            var playerStage = cap.resolve();

            if (playerStage.isPresent()) {
                playerStage.get().addStage(stage);
                playerStage.get().setChangedFor(player, PlayerStage.Operation.ADD, stage);
            }
        }
    }

    /**
     * @author Alessandro
     * @reason AStages integration
     */
    @Overwrite
    public void removeGameStage(@NotNull Player player, String stage) {
        var cap = player.getCapability(PlayerStageProvider.PLAYER_STAGE);

        if (cap.isPresent()) {
            var playerStage = cap.resolve();

            if (playerStage.isPresent()) {
                playerStage.get().removeStage(stage);
                playerStage.get().setChangedFor(player, PlayerStage.Operation.REMOVE, stage);
            }
        }
    }
}
