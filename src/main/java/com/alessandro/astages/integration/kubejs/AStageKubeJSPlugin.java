package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.stage.AStageManager;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.integration.kubejs.util.KubeJSStageEventHandler;
import com.alessandro.astages.integration.kubejs.util.StageEvents;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.ATime;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AStageKubeJSPlugin extends KubeJSPlugin {
    static {
        if (Mods.KUBEJS.isLoaded()) {
            KubeJSStageEventHandler.init();
        }
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        if (!Mods.KUBEJS.isLoaded()) return;

        if (event.getType().isServer() || event.getType().isStartup()) {
            event.add("AStages", AStagesKubeJSUtil.class);
            event.add("Attributes", Attributes.class);
            event.add("ItemAttributes", Attributes.Item.class);
            event.add("ScreenAttributes", Attributes.Screen.class);
            event.add("PetAttributes", Attributes.Pet.class);
            event.add("DimensionAttributes", Attributes.Dimension.class);
            event.add("StructureAttributes", Attributes.Structure.class);
            event.add("ATime", ATime.class);
        }

        if (event.getType().isClient()) {
            event.add("AStagesClient", AStagesClientJSUtil.class);
        }

        event.add("AModels", AStagesModelJSUtil.class);
    }

    @Override
    public void registerEvents() {
        if (!Mods.KUBEJS.isLoaded()) return;

        StageEvents.GROUP.register();
    }

    @Override
    public void init() {
        if (!Mods.KUBEJS.isLoaded()) return;

        AStages.LOGGER.debug("ASTAGES-KUBEJS: INITIALIZED PLUGIN!");
    }

    @Override
    public void onServerReload() {
        // AFTER SERVER SCRIPT RELOADING!
        AStageManager.reloadAfterScripts();
        ARestrictionManager.reloadAfterScripts();
    }

    @Override
    public void clearCaches() {
        // BEFORE SERVER SCRIPT RELOADING!
        AStageManager.reloadBeforeScripts();
        ARestrictionManager.reloadBeforeScripts();
    }
}
