package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.client.AClientItemManager;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.ud.IsItemRestrictedC2SPacket;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
//    public static void tooltip(ItemTooltipEvent event) {
//        event.getItemStack().getItemHolder().is()
//    }

    @SubscribeEvent
    public static void onItemTooltip(@NotNull ItemTooltipEvent event) {
        if (event.getEntity() != null) {
//            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getItemStack());
//            if (restriction == null) { return; }
//
//            if (restriction.hideTooltip) {
//                event.getToolTip().clear();
//                event.getToolTip().add(restriction.getHiddenName(event.getItemStack()));
//            }

            var stack = event.getItemStack();
//            var restriction = AClientRestrictionManager.ITEM_INSTANCE.isTooltipRestricted(stack);
            var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(stack);

            if (restriction != null && restriction.hideTooltip()) {
                event.getToolTip().clear();
                event.getToolTip().add(restriction.tooltipMessage());
//                AStages.LOGGER.debug(stack.toString());
            }
        }
    }
}
