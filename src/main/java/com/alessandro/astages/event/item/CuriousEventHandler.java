package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class CuriousEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
//        event.player.isColliding()
//
//
//        event.player.knockback(1, );

//        CuriosApi.getCuriosInventory(event.player).ifPresent(curios -> {
//            curios.getCurios().forEach((slotId, stacksHandler) -> {
//                // stacksHandler.getStacks().setPreviousStackInSlot();
//                stacksHandler.getStacks().extractItem(stacksHandler.getStacks().getSlots(), 1, false);
//                event.player.drop(stacksHandler.getStacks().getStackInSlot(stacksHandler.getSlots()), false);
//            });
//        });
    }
}
