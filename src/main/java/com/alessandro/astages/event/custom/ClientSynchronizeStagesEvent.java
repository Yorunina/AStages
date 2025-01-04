package com.alessandro.astages.event.custom;

import com.alessandro.astages.util.develop.Info;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

@Info("Sync event for client!")
public class ClientSynchronizeStagesEvent extends Event {
    final List<String> stagesSynced;

    public ClientSynchronizeStagesEvent(List<String> stagesSynced) {
        this.stagesSynced = stagesSynced;
    }

    public List<String> getStagesSynced() {
        return stagesSynced;
    }
}
