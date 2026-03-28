package com.alessandro.astages.infrastructure.integration.kubejs.bridge;

import com.alessandro.astages.infrastructure.integration.kubejs.event.KubeJSStageAddedEvent;
import com.alessandro.astages.infrastructure.integration.kubejs.event.KubeJSStageRemovedEvent;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.Extra;

public class KubeJSStageEvents {
    public static final EventGroup GROUP = EventGroup.of("AStageEvents");

    public static final EventHandler STAGE_ADDED = GROUP.server("added", () -> KubeJSStageAddedEvent.class).extra(Extra.ID);
    public static final EventHandler STAGE_REMOVED = GROUP.server("removed", () -> KubeJSStageRemovedEvent.class).extra(Extra.ID);
}
