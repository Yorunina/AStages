package com.alessandro.astages.renderer;


import com.alessandro.astages.AStages;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class RenderLevelEvent {
//    @SubscribeEvent
    public static void renderLevel(@NotNull RenderLevelStageEvent event) {
        // if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            // AStages.LOGGER.debug("Instantiated!");
            // ARenderer.requestRefresh = true;
        ARenderer.renderBlocks(event.getPoseStack(), event.getProjectionMatrix());
        // }
    }
}
