package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AOreRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.client.AClientOreRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.RenderAtLoginS2CPacket;
import com.alessandro.astages.render.AOreBakedModel;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.Info;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.EffectiveSide;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
@ParametersAreNonnullByDefault
public class ClientEventHandler {
    static {
        if (EffectiveSide.get().isClient()) {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientOreUpdateEvent.class, e -> renderAllAgain());
        }
    }

    @Info("TODO: SYNCHRONIZE ORE STAGES!")
    @SubscribeEvent
    public static void stageSync(ClientSynchronizeStagesEvent event) {
        if (!AClientRestrictionManager.areOreStages(event.getStagesSynced())) { return; }

        renderAllAgain();
    }

    public static void renderAllAgain() {
        for (Map.Entry<String, List<AClientOreRestriction>> entry : AClientRestrictionManager.ORE_INSTANCE.restrictions.entrySet()) {
            for (AClientOreRestriction restriction : entry.getValue()) {
                AStagesUtil.setBakedModelForState(restriction.original(), new AOreBakedModel(entry.getKey(), restriction.original(), restriction.replacement()));
            }
        }

        // Minecraft.getInstance().levelRenderer. // Try reloading only blocks
        Minecraft.getInstance().levelRenderer.allChanged();
    }
}
