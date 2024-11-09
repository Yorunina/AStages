package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onItemTooltip(@NotNull ItemTooltipEvent event) {
        if (event.getEntity() != null) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getItemStack());
            if (restriction == null) { return; }

            if (restriction.hideTooltip) {
                event.getToolTip().clear();
                event.getToolTip().add(restriction.getHiddenName(event.getItemStack()));
            }
        }
    }
}
