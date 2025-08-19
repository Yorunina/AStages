package com.alessandro.astages.event.custom.types;

import com.alessandro.astages.capability.PlayerStage;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class ClientStageEvent extends Event {
    private final List<String> stagesSynced;
    private final PlayerStage.Operation operation;

    public ClientStageEvent(List<String> stagesSynced, PlayerStage.Operation operation) {
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
