package com.alessandro.astages.event.dimension;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.ADimensionRestriction;
import com.alessandro.astages.store.Attributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void onEntityTravel(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof Player player) {
            ResourceLocation dimension = event.getDimension().location();

            ResourceLocation currentDimension = player.level().dimension().location();
            ADimensionRestriction fromDim = ARestrictionManager.DIMENSION_INSTANCE.getRestriction(player, currentDimension);
            ADimensionRestriction toDim = ARestrictionManager.DIMENSION_INSTANCE.getRestriction(player, dimension);

            if (fromDim != null && fromDim.isEnabled(Attributes.BIDIRECTIONAL)) {
                event.setCanceled(true);
                fromDim.displayMessage(Attributes.Dimension.LEAVE_MESSAGE, dimension, player);
            } else if (toDim != null) {
                event.setCanceled(true);
                toDim.displayMessage(Attributes.Dimension.ENTER_MESSAGE, dimension, player);
            }
        }
    }
}
