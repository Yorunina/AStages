package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AStages.MODID)
public class CuriousEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        // NOT INTEGRATED, TRY LATER WITH AN ADDON!
    }
}
