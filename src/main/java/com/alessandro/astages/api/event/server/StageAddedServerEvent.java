package com.alessandro.astages.api.event.server;

import com.alessandro.astages.api.event.custom.ServerEvent;
import net.minecraft.server.MinecraftServer;

public class StageAddedServerEvent extends ServerEvent {
    public final String stage;

    public StageAddedServerEvent(MinecraftServer server, String stage) {
        super(server);
        this.stage = stage;
    }
}
