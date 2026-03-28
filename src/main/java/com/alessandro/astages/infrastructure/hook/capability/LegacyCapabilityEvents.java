package com.alessandro.astages.infrastructure.hook.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.infrastructure.capability.PlayerStageWrapper;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class LegacyCapabilityEvents {
    @SubscribeEvent
    public static void onAttachedCapabilities(AttachCapabilitiesEvent<Entity> event) {
        PlayerStageWrapper.onAttachedCapabilities(event);
    }
}
