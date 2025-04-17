package com.alessandro.astages.datageneration;

import com.alessandro.astages.AStages;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = AStages.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GatherDataEventHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.getGenerator().addProvider(
            event.includeClient(),
            new ALanguageProvider(packOutput, "en_us")
        );
    }
}
