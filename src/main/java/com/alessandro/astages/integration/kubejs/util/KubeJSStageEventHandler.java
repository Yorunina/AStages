package com.alessandro.astages.integration.kubejs.util;

import com.alessandro.astages.event.custom.actions.StageAddedPlayerEvent;
import com.alessandro.astages.event.custom.actions.StageRemovedPlayerEvent;
import com.alessandro.astages.integration.kubejs.event.StageAddedEventJS;
import com.alessandro.astages.integration.kubejs.event.StageRemovedEventJS;
import dev.latvian.mods.kubejs.script.ScriptTypeHolder;
import net.minecraftforge.common.MinecraftForge;

public class KubeJSStageEventHandler {
    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(KubeJSStageEventHandler::stageAdded);
        MinecraftForge.EVENT_BUS.addListener(KubeJSStageEventHandler::stageRemoved);
    }

    public static void stageAdded(StageAddedPlayerEvent event) {
        if (StageEvents.STAGE_ADDED.hasListeners()) {
            StageEvents.STAGE_ADDED.post((ScriptTypeHolder) event.getEntity(), event.stage, new StageAddedEventJS(event));
        }
    }

    public static void stageRemoved(StageRemovedPlayerEvent event) {
        if (StageEvents.STAGE_REMOVED.hasListeners()) {
            StageEvents.STAGE_REMOVED.post((ScriptTypeHolder) event.getEntity(), event.stage, new StageRemovedEventJS(event));
        }
    }
}
