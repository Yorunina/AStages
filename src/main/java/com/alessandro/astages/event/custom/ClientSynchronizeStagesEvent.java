package com.alessandro.astages.event.custom;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.event.custom.types.ClientStageEvent;
import com.alessandro.astages.util.develop.Info;

import java.util.List;

@Info("Sync event for client!")
public class ClientSynchronizeStagesEvent extends ClientStageEvent {
    public ClientSynchronizeStagesEvent(List<String> stagesSynced, PlayerStage.Operation operation) {
        super(stagesSynced, operation);
    }
}
