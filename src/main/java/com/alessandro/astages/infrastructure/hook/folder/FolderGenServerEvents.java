package com.alessandro.astages.infrastructure.hook.folder;

import com.alessandro.astages.AStages;
import com.alessandro.astages.infrastructure.folder.AStagesFolderSystem;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class FolderGenServerEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStarting(ServerAboutToStartEvent event) {
        var server = event.getServer();
        AStagesFolderSystem.buildServerPaths(server);
    }
}
