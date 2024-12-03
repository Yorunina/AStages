package com.alessandro.astages.simple;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.util.Info;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ForgeEventHandler {
    @SubscribeEvent
    public static void serverLoading(ServerStartingEvent event) {
        ASimpleRestrictionManager.readFromFile();
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppingEvent event) {
        ASimpleRestrictionManager.writeToFile();
    }

    public static int tick = 0;
    @SubscribeEvent
    @Info("For Debug Only!")
    public static void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (event.side.isClient()) {
            if (tick == 20) {
                AStages.LOGGER.debug(AClientRestrictionManager.ORE_STAGES.toString());
//                AStages.LOGGER.debug(ASimpleRestrictionManager.RESTRICTIONS.toString());
//                AStages.LOGGER.debug(ARestrictionManager.PET_INSTANCE.toString());

//                AStages.LOGGER.debug("CLIENT: {}", AClientRestrictionManager.RECIPE_INSTANCE.restrictions);
//                tick = 0;
            }

//            tick++;
        }

        if (event.side.isServer()) {
            if (tick == 20) {
//                AStages.LOGGER.debug(ASimpleRestrictionManager.RESTRICTIONS.toString());
//                AStages.LOGGER.debug(ARestrictionManager.PET_INSTANCE.toString());

//                AStages.LOGGER.debug("SERVER: {}", ARestrictionManager.RECIPE_INSTANCE.getRestrictions());
                tick = 0;
            }

            tick++;
        }
    }
}
