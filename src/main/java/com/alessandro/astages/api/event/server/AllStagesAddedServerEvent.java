package com.alessandro.astages.api.event.server;

import com.alessandro.astages.api.event.custom.ServerEvent;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class AllStagesAddedServerEvent extends ServerEvent {
    public final List<String> stages;

    public AllStagesAddedServerEvent(MinecraftServer server, List<String> stages) {
        super(server);
        this.stages = stages;
    }
}
