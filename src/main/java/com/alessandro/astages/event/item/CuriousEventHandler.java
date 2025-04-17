package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = AStages.MODID)
public class CuriousEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
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
