package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AStagesFolderSystem;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onServerStarting(ServerAboutToStartEvent event) {
        var server = event.getServer();
        AStagesFolderSystem.buildPaths(server);
    }
}
