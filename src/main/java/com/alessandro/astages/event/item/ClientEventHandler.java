package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.store.Attributes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    public static boolean jeiGetter = false;

    @SubscribeEvent
    public static void onItemTooltip(@NotNull ItemTooltipEvent event) {
        if (event.getEntity() != null && !jeiGetter) {
            var stack = event.getItemStack();
            var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(stack);
            var properties = AClientRestrictionManager.ITEM_INSTANCE.getProperties(stack);

            if (restriction != null && properties != null && restriction.isEnabled(Attributes.HIDING_TOOLTIP)) {
                event.getToolTip().clear();
                event.getToolTip().add(properties.hiddenName());
            }
        }
    }
}
