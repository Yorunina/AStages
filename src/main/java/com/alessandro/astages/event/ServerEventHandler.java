package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void onServerStarting(ServerStartedEvent event) {
        // ServerStageData.getData(event.getServer()).add("test_stage_1", "test_mob");
    }
}
