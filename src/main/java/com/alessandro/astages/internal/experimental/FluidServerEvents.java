package com.alessandro.astages.internal.experimental;

import com.alessandro.astages.AStages;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class FluidServerEvents {
    public static void onEvent(BlockEvent.FluidPlaceBlockEvent event) {
    }

    public static void onEvent(FillBucketEvent event) {

    }

    public static void onEvent(BlockEvent.CreateFluidSourceEvent event) {
        // event.getLevel().getBiome(event.getPos()).is
        // event.getState().getFluidState().getType()
    }

    public static void onEvent(PlayerInteractEvent event) {
//        var stack = event.getItemStack();
//
//        if (stack.isEdible() && event.getEntity().eat())
    }
}
