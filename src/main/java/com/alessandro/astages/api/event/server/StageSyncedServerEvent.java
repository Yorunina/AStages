package com.alessandro.astages.api.event.server;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.event.custom.ServerEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.Collections;
import java.util.List;

@Cancelable
public class StageSyncedServerEvent extends ServerEvent {
    final AOperation operation;
    final List<String> stagesSynced;

    public StageSyncedServerEvent(MinecraftServer server, AOperation operation, String stageSynced) {
        this(server, operation, Collections.singletonList(stageSynced));
    }

    public StageSyncedServerEvent(MinecraftServer server, AOperation operation, List<String> stagesSynced) {
        super(server);
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
