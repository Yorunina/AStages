package com.alessandro.astages.command;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class AStagesCommands {
    @SubscribeEvent
    public static void commandRegisterEvent(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        var context = event.getBuildContext();

        AStagesModificationCommands.register(dispatcher);
        AStagesSimpleRestrictionsCommands.register(dispatcher, context);
        AStagesServerCommands.register(dispatcher);
        AStagesTimerCommands.register(dispatcher);
        AStagesInfoCommands.register(dispatcher);
    }
}
