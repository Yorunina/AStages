package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.AClientOreRestriction;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import com.alessandro.astages.render.AOreBakedModel;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.common.NeoForge;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
@ParametersAreNonnullByDefault
public class ClientEventHandler {
    private static final Map<BlockState, BakedModel> changes = new HashMap<>();

    static {
        if (EffectiveSide.get().isClient()) {
            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientOreUpdateEvent.class, e -> renderAllAgain());
        }
    }

    @SubscribeEvent
    public static void stageSync(ClientSynchronizeStagesEvent event) {
        if (!AClientRestrictionManager.areOreStages(event.getStagesSynced())) { return; }

        renderAllAgain();
    }

    public static void renderAllAgain() {
        if (!changes.isEmpty()) {
            for (var state : changes.keySet()) {
                AStagesUtil.setBakedModelForState(state, changes.get(state));
            }

            changes.clear();
        }

        for (var entry : AClientRestrictionManager.ORE_INSTANCE.getRestrictionsByStage().entrySet()) {
            for (AClientOreRestriction restriction : entry.getValue()) {
                changes.put(restriction.getOriginal(), Minecraft.getInstance().getBlockRenderer().getBlockModel(restriction.getOriginal()));
                AStagesUtil.setBakedModelForState(restriction.getOriginal(), new AOreBakedModel(entry.getKey(), restriction.getOriginal(), restriction.getReplacement()));
            }
        }

        // Minecraft.getInstance().levelRenderer. // Try reloading only blocks
        Minecraft.getInstance().levelRenderer.allChanged();
    }
}
