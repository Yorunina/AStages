package com.alessandro.astages.infrastructure.hook.simple;

import com.alessandro.astages.AStages;
import com.alessandro.astages.engine.simple.ASimpleMigrationManager;
import com.alessandro.astages.engine.ASimpleRestrictionManager;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class SimpleServerEvents {
    @SubscribeEvent
    public static void serverLoading(ServerStartingEvent event) {
        ASimpleMigrationManager.startMigration();
        ASimpleRestrictionManager.readFromFile();
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppingEvent event) {
        ASimpleRestrictionManager.writeToFile(true);
    }
}
