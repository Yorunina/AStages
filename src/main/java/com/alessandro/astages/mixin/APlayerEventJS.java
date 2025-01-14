package com.alessandro.astages.mixin;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;

// @Mixin(value = KubePlayerEvent.class, remap = false)
@Mixin(value = PlayerInteractEvent.RightClickBlock.class, remap = false)
public abstract class APlayerEventJS {
//    @Shadow public abstract @Nullable Player getPlayer();

    /**
     * @author Alessandro
     * @reason support new stage system
     */
//    @Info("Checks if the player has the specified game stage")
//    // @Inject(method = "hasGameStage", at = @At("HEAD"))
//    @Overwrite
//    public boolean hasGameStage(String stage) {
//        var data = Objects.requireNonNull(getPlayer()).getData(AProvider.PLAYER_STAGE);
//
//        return data.getStages().contains(stage);
//    }

    /**
     * @author Alessandro
     * @reason support new stage system
     */
//    @Info("Adds the specified game stage to the player")
//    @Overwrite
//    public void addGameStage(String stage) {
//        var data = Objects.requireNonNull(getPlayer()).getData(AProvider.PLAYER_STAGE);
//        data.addStage(stage);
//        data.setChangedFor(getPlayer(), PlayerStage.Operation.ADD, stage);
//    }

    /**
     * @author Alessandro
     * @reason support new stage system
     */
//    @Info("Removes the specified game stage from the player")
//    @Overwrite
//    public void removeGameStage(String stage) {
//        var data = Objects.requireNonNull(getPlayer()).getData(AProvider.PLAYER_STAGE);
//        data.removeStage(stage);
//        data.setChangedFor(getPlayer(), PlayerStage.Operation.REMOVE, stage);
//    }
}
