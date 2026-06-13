package com.alessandro.astages.infrastructure.hook.advancement;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.event.player.StageAddedPlayerEvent;
import com.alessandro.astages.api.event.server.StageAddedServerEvent;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.AServerUtils;
import com.alessandro.astages.infrastructure.registry.ACriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class AdvancementServerEvents {
    @SubscribeEvent
    public static void onStageAdded(StageAddedPlayerEvent event) {
        ACriteriaTriggers.STAGE_EARN.trigger((ServerPlayer) event.getPlayer());
    }

    @SubscribeEvent
    public static void onStageAdded(StageAddedServerEvent event) {
        AServerUtils.forEachPlayer(
            event.getServer(),
            ACriteriaTriggers.STAGE_EARN::trigger
        );
    }

    public static void onAdvancementEarned(AdvancementEvent.AdvancementEarnEvent event) {

    }
}