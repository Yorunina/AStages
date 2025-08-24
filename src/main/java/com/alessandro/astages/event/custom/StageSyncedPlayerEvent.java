package com.alessandro.astages.event.custom;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.develop.Info;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.Collections;
import java.util.List;

@Info("Sync event for server!")
@Cancelable
public class StageSyncedPlayerEvent extends PlayerEvent {
    final AOperation operation;
    final List<String> stagesSynced;

    public StageSyncedPlayerEvent(Player player, AOperation operation, String stageSynced) {
        this(player, operation, Collections.singletonList(stageSynced));
    }

    public StageSyncedPlayerEvent(Player player, AOperation operation, List<String> stagesSynced) {
        super(player);
        this.operation = operation;
        this.stagesSynced = stagesSynced;
    }

    public AOperation getOperation() {
        return operation;
    }

    public List<String> getStagesSynced() {
        return stagesSynced;
    }
}
