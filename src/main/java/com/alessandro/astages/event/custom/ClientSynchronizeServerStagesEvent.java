package com.alessandro.astages.event.custom;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.event.custom.types.ClientStageEvent;

import java.util.List;

public class ClientSynchronizeServerStagesEvent extends ClientStageEvent {
    public ClientSynchronizeServerStagesEvent(List<String> serverStagesSynced, PlayerStage.Operation operation) {
        super(serverStagesSynced, operation);
    }
}
