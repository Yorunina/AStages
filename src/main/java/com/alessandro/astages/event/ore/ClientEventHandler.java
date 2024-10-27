package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AOreRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.RenderAtLoginS2CPacket;
import com.alessandro.astages.render.AOreBakedModel;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void stageSync(@NotNull ClientSynchronizeStagesEvent event) {
        if (!ARestrictionManager.areOreStages(event.getStagesSynced())) { return; }

        renderAllAgain();
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.@NotNull PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ModNetworking.sendToPlayer(new RenderAtLoginS2CPacket(), player);
        }
    }

    public static void renderAllAgain() {
        for (Map.Entry<String, List<AOreRestriction>> entry : ARestrictionManager.ORE_INSTANCE.getRestrictions().entrySet()) {
            for (AOreRestriction restriction : entry.getValue()) {
                AStagesUtil.setBakedModelForState(restriction.original, new AOreBakedModel(entry.getKey(), restriction.original, restriction.replacement));
            }
        }

        Minecraft.getInstance().levelRenderer.allChanged();
    }
}
