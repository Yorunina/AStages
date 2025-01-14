package com.alessandro.astages.command;

import com.alessandro.astages.AStages;
import com.alessandro.astages.simple.AStagesSimpleRestrictionsCommands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = AStages.MODID)
public class AStagesCommands {
    @SubscribeEvent
    public static void commandRegisterEvent(@NotNull RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        var context = event.getBuildContext();

        AStagesModificationCommands.register(dispatcher);
        AStagesSimpleRestrictionsCommands.register(dispatcher, context);
    }
}
