package com.alessandro.astages.event.fluid;

import com.alessandro.astages.AStages;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    public static void onEvent(BlockEvent.FluidPlaceBlockEvent event) {

    }

    public static void onEvent(FillBucketEvent event) {

    }

    public static void onEvent(BlockEvent.CreateFluidSourceEvent event) {
        // event.getLevel().getBiome(event.getPos()).is
        // event.getState().getFluidState().getType()
    }
}
