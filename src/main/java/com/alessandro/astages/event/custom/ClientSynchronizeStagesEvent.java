package com.alessandro.astages.event.custom;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.util.develop.Info;
import net.neoforged.bus.api.Event;

import java.util.List;

@Info("Sync event for client!")
public class ClientSynchronizeStagesEvent extends Event {
    private final List<String> stagesSynced;
    private final PlayerStage.Operation operation;

    public ClientSynchronizeStagesEvent(List<String> stagesSynced, PlayerStage.Operation operation) {
        this.stagesSynced = stagesSynced;
        this.operation = operation;
    }

    public List<String> getStagesSynced() {
        return stagesSynced;
    }

    public PlayerStage.Operation getOperation() {
        return operation;
    }
}
