package com.alessandro.astages.mixin;

import com.alessandro.astages.capability.AProvider;
import com.alessandro.astages.capability.PlayerStage;
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
        var playerStage = player.getData(AProvider.PLAYER_STAGE);
        return playerStage.getStages().contains(stage);
    }

    /**
     * @author Alessandro
     * @reason AStages integration
     */
    @Overwrite
    public void addGameStage(@NotNull Player player, String stage) {
        var playerStage = player.getData(AProvider.PLAYER_STAGE);

        playerStage.addStage(stage);
        playerStage.setChangedFor(player, PlayerStage.Operation.ADD, stage);
    }

    /**
     * @author Alessandro
     * @reason AStages integration
     */
    @Overwrite
    public void removeGameStage(@NotNull Player player, String stage) {
        var playerStage = player.getData(AProvider.PLAYER_STAGE);

        playerStage.removeStage(stage);
        playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE, stage);
    }
}
