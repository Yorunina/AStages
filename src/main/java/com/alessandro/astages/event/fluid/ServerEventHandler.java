package com.alessandro.astages.event.fluid;

import com.alessandro.astages.AStages;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.block.CreateFluidSourceEvent;

@EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onEvent(BlockEvent.FluidPlaceBlockEvent event) {

    }

//    public static void onEvent(FillBucketEvent event) {
//
//    }

    public static void onEvent(CreateFluidSourceEvent event) {
        // event.getLevel().getBiome(event.getPos()).is
        // event.getState().getFluidState().getType()
    }
}
