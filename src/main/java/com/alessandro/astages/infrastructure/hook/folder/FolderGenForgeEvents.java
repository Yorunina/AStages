package com.alessandro.astages.infrastructure.hook.folder;

import com.alessandro.astages.AStages;
import com.alessandro.astages.infrastructure.folder.AStagesFolderSystem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(modid = AStages.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FolderGenForgeEvents {
    @SubscribeEvent
    public static void onConfig(FMLLoadCompleteEvent event) {
        AStagesFolderSystem.buildConfigPaths();
    }
}
