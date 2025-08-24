package com.alessandro.astages.event.custom;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.event.custom.types.ClientStageEvent;

import java.util.List;

public class ClientSynchronizeServerStagesEvent extends ClientStageEvent {
    public ClientSynchronizeServerStagesEvent(List<String> serverStagesSynced, AOperation operation) {
        super(serverStagesSynced, operation);
    }
}
