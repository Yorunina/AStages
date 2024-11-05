package com.alessandro.astages.event;

import com.alessandro.astages.Astages;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Astages.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void registerCapabilities(@NotNull RegisterCapabilitiesEvent event) {
//        event.registerEntity(
//            PlayerStageProvider.PLAYER_STAGE,
//            EntityType.PLAYER,
//            (myEntity, context) -> new PlayerStage()
//        );
    }

}
