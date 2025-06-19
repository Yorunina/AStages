package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.develop.ToBeTested;
import com.alessandro.astages.util.develop.UnderDevelopment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.event.CurioEquipEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ToBeTested
@UnderDevelopment
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class CuriosEventHandler {
//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        var player = event.player;
//
//        player.getCapability(CuriosCapability.INVENTORY).ifPresent(inventoryHandler -> {
//            var allCurios = inventoryHandler.getCurios(); // Represents each slot! (head, necklace, body and so on...)
//
//            for (var curio : allCurios.values()) {
//                var stacks = curio.getStacks();
//                AStages.LOGGER.debug(stacks.toString());
//
//                for (int slot = 0; slot < stacks.getSlots(); slot++) {
//                    var stack = stacks.getStackInSlot(slot);
//                    var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, stack);
//
//                    if (restriction != null) {
//                        restriction.displayMessage(Attributes.Item.CURIOS_MESSAGE, stack, player);
//
//                        var copiedStack = stack.copy(); // I don't know if it's necessary!
//                        stacks.setStackInSlot(slot, ItemStack.EMPTY);
//                        player.drop(copiedStack, false);
//                    }
//                }
//            }
//        });
//    }

    @SubscribeEvent
    public static void onCurioEquip(CurioEquipEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var curio = event.getStack();
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, curio);

            AStages.LOGGER.debug(curio.toString());

            if (restriction != null && restriction.isDisabled(Attributes.CURIO_EQUIPPING)) {
                event.setResult(Event.Result.DENY);
                restriction.displayMessage(Attributes.Item.CURIOS_MESSAGE, curio, player);
            }
        }
    }
}
