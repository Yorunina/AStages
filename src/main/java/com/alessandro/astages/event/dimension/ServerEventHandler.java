package com.alessandro.astages.event.dimension;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.ADimensionRestriction;
import com.alessandro.astages.store.Attributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void onEntityTravel(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof Player player) {
            ResourceLocation dimension = event.getDimension().location();

            ADimensionRestriction restriction = ARestrictionManager.DIMENSION_INSTANCE.getRestriction(player, dimension);
//            ADimensionRestriction restrictionForCurrentDimension = ARestrictionManager.DIMENSION_INSTANCE.getRestriction(currentDimension);

//            if (restriction != null) {
//                var isBidirectional = restriction.bidirectional;
//
//                if (isBidirectional) {
//
//                }
//            }

            // if (restriction != null && !restriction.bidirectional) {
            if (restriction != null) {
                event.setCanceled(true);

                restriction.displayMessage(Attributes.Dimension.ENTER_MESSAGE, dimension, player);
            }
        }
    }
}
