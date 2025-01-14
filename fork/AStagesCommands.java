package com.alessandro.astages.command;

import com.alessandro.astages.Astages;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Astages.MODID)
public class AStagesCommands {
    @SubscribeEvent
    public static void commandRegisterEvent(@NotNull RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        AStagesModificationCommands.register(dispatcher);
    }
}
