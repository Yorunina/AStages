package com.alessandro.astages.event.custom;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Collections;
import java.util.List;

@Info("Sync event for server!")
public class StageSyncedPlayerEvent extends PlayerEvent implements ICancellableEvent {
    final PlayerStage.Operation operation;
    final List<String> stagesSynced;

    public StageSyncedPlayerEvent(Player player, PlayerStage.Operation operation, String stageSynced) {
        this(player, operation, Collections.singletonList(stageSynced));
    }

    public StageSyncedPlayerEvent(Player player, PlayerStage.Operation operation, List<String> stagesSynced) {
        super(player);
        this.operation = operation;
        this.stagesSynced = stagesSynced;
    }

    public PlayerStage.Operation getOperation() {
        return operation;
    }

    public List<String> getStagesSynced() {
        return stagesSynced;
    }
}
