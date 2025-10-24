package com.alessandro.astages.datageneration;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GatherDataEventHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.getGenerator().addProvider(
            event.includeClient(),
            new ALanguageProvider(packOutput, "en_us")
        );

//        event.getGenerator().addProvider(
//            event.includeServer(),
//            new ALootProvider(packOutput)
//        );
    }
}
