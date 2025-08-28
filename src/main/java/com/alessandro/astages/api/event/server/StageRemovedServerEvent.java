package com.alessandro.astages.api.event.server;

import com.alessandro.astages.api.event.custom.ServerEvent;
import net.minecraft.server.MinecraftServer;

public class StageRemovedServerEvent extends ServerEvent {
    public final String stage;

    public StageRemovedServerEvent(MinecraftServer server, String stage) {
        super(server);
        this.stage = stage;
    }
}
