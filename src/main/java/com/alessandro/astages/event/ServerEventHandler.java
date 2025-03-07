package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void onServerStarting(ServerStartedEvent event) {
        // ServerStageData.getData(event.getServer()).add("test_stage_1", "test_mob");
    }
}
