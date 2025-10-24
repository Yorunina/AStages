package com.alessandro.astages.api.event.server;

import com.alessandro.astages.api.ASetUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.event.custom.ServerEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.Set;

@Cancelable
public class StageSyncedServerEvent extends ServerEvent {
    final AOperation operation;
    final Set<String> stagesSynced;

    public StageSyncedServerEvent(MinecraftServer server, AOperation operation, String stageSynced) {
        this(server, operation, ASetUtils.singleton(stageSynced));
    }

    public StageSyncedServerEvent(MinecraftServer server, AOperation operation, Set<String> stagesSynced) {
        super(server);
        this.operation = operation;
        this.stagesSynced = stagesSynced;
    }

    public AOperation getOperation() {
        return operation;
    }

    public Set<String> getStagesSynced() {
        return stagesSynced;
    }
}
