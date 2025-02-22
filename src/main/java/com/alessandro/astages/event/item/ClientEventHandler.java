package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.util.develop.Info;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Info("To be RE-IMPLEMENTED!")
@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onItemTooltip(@NotNull ItemTooltipEvent event) {
        if (event.getEntity() != null) {
            var stack = event.getItemStack();
            // var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(stack);

//            if (restriction != null && restriction.hideTooltip()) {
//                event.getToolTip().clear();
//                event.getToolTip().add(restriction.tooltipMessage());
//            }
        }
    }
}
