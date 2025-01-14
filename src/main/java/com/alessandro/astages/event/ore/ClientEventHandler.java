package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.client.AClientOreRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import com.alessandro.astages.render.AOreBakedModel;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.common.NeoForge;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
@ParametersAreNonnullByDefault
public class ClientEventHandler {
    static {
        if (EffectiveSide.get().isClient()) {
            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientOreUpdateEvent.class, e -> renderAllAgain());
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
