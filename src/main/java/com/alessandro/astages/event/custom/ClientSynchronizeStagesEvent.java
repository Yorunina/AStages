package com.alessandro.astages.event.custom;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.event.custom.types.ClientStageEvent;
import com.alessandro.astages.api.develop.Info;

import java.util.List;

@Info("Sync event for client!")
public class ClientSynchronizeStagesEvent extends ClientStageEvent {
    public ClientSynchronizeStagesEvent(List<String> stagesSynced, AOperation operation) {
        super(stagesSynced, operation);
    }
}
