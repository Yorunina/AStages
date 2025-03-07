package com.alessandro.astages.renderer;


import com.alessandro.astages.AStages;
import com.alessandro.astages.util.develop.UnderDevelopment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

@UnderDevelopment
@EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class RenderLevelEvent {
    @SubscribeEvent
    public static void renderLevel(@NotNull RenderLevelStageEvent event) {
        // if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            // AStages.LOGGER.debug("Instantiated!");
            // ARenderer.requestRefresh = true;


        // ARenderer.renderBlocks(event.getPoseStack(), event.getProjectionMatrix()); // <- THIS LINE!


        // }
    }
}
