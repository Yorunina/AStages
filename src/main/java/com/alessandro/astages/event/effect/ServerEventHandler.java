package com.alessandro.astages.event.effect;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void effectAdded(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var effect = event.getEffectInstance().getEffect();
            var restriction = ARestrictionManager.EFFECT_INSTANCE.getRestriction(effect, player, player.getServer());

            if (restriction != null) {
                event.setResult(Event.Result.DENY);
                player.onEffectRemoved(event.getEffectInstance());
            }
        }
    }
}
