package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() != null && AClientRestrictionManager.didJeiFinishReloading()) {
            var stack = event.getItemStack();
            var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), stack);
            var properties = AClientRestrictionManager.ITEM_INSTANCE.getProperties(AClientHolder.serverAndPlayer(), stack);

            if (restriction != null && properties != null && restriction.isEnabled(Attributes.HIDING_TOOLTIP)) {
                event.getToolTip().clear();
                event.getToolTip().add(properties.hiddenName());
            }
        }
    }
}
