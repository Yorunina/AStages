package com.alessandro.astages.mixin;

import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.Nullable;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = PlayerEventJS.class, remap = false)
public abstract class APlayerEventJS {
    @Shadow public abstract @Nullable Player getPlayer();

    /**
     * @author Alessandro
     * @reason support new stage system
     */
    @Info("Checks if the player has the specified game stage")
    @Overwrite
    public boolean hasGameStage(String stage) {
        return AStagesUtils.hasStage(AHolder.player(getPlayer()), stage);
    }

    /**
     * @author Alessandro
     * @reason support new stage system
     */
    @Info("Adds the specified game stage to the player")
    @Overwrite
    public void addGameStage(String stage) {
        AStagesUtils.addStage(AHolder.player(getPlayer()), stage, false);
    }

    /**
     * @author Alessandro
     * @reason support new stage system
     */
    @Info("Removes the specified game stage from the player")
    @Overwrite
    public void removeGameStage(String stage) {
        AStagesUtils.removeStage(AHolder.player(getPlayer()), stage, false);
    }
}
