package com.alessandro.astages.simple;

import com.alessandro.astages.AStages;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ForgeEventHandler {
    @SubscribeEvent
    public static void serverLoading(ServerStartingEvent event) {
        ASimpleRestrictionManager.readFromFile();
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppingEvent event) {
        ASimpleRestrictionManager.writeToFile();
    }
}
