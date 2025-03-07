package com.alessandro.astages.event.effect;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void effectAdded(MobEffectEvent.Applicable event) {
        var effectInstance = event.getEffectInstance();
        if (event.getEntity() instanceof ServerPlayer player && effectInstance != null) {
            var effect = effectInstance.getEffect();
            var restriction = ARestrictionManager.EFFECT_INSTANCE.getRestriction(player, effect.value());

            if (restriction != null) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }
    }
}
