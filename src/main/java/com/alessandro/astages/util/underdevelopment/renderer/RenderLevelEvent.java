package com.alessandro.astages.util.underdevelopment.renderer;


import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.UnderDevelopment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@UnderDevelopment
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
