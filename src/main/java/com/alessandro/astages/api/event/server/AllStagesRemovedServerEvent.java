package com.alessandro.astages.api.event.server;

import com.alessandro.astages.api.event.custom.ServerEvent;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class AllStagesRemovedServerEvent extends ServerEvent {
    public final List<String> stages;

    public AllStagesRemovedServerEvent(MinecraftServer server, List<String> stages) {
        super(server);
        this.stages = stages;
    }
}
