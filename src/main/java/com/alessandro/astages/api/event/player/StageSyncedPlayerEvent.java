package com.alessandro.astages.api.event.player;

import com.alessandro.astages.api.util.ASetUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.event.custom.PlayerEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.Set;

@Info("Sync event for server!")
@Cancelable
public class StageSyncedPlayerEvent extends PlayerEvent {
    final AOperation operation;
    final Set<String> stagesSynced;

    public StageSyncedPlayerEvent(Player player, AOperation operation, String stageSynced) {
        this(player, operation, ASetUtils.singleton(stageSynced));
    }

    public StageSyncedPlayerEvent(Player player, AOperation operation, Set<String> stagesSynced) {
        super(player);
        this.operation = operation;
        this.stagesSynced = stagesSynced;
    }

    public AOperation getOperation() {
        return operation;
    }

    public Set<String> getStagesSynced() {
        return stagesSynced;
    }
}
