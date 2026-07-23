package com.alessandro.astages.infrastructure.hook.config;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.config.AStagesClient;
import com.alessandro.astages.infrastructure.integration.RecipeViewerMods;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ConfigReloadedEvents {
    @SubscribeEvent
    public static void onConfigReloaded(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == AStagesClient.SPEC) {
            RecipeViewerMods.clearCache();
        }
    }
}