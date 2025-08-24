package com.alessandro.astages.event.custom.types;

import com.alessandro.astages.api.constant.AOperation;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class ClientStageEvent extends Event {
    private final List<String> stagesSynced;
    private final AOperation operation;

    public ClientStageEvent(List<String> stagesSynced, AOperation operation) {
        this.stagesSynced = stagesSynced;
        this.operation = operation;
    }

    public List<String> getStagesSynced() {
        return stagesSynced;
    }

    public AOperation getOperation() {
        return operation;
    }
}
