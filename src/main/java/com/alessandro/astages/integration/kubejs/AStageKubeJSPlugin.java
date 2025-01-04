package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.integration.kubejs.util.KubeJSStageEventHandler;
import com.alessandro.astages.integration.kubejs.util.StageEvents;
import com.alessandro.astages.store.Attributes;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import org.jetbrains.annotations.NotNull;

public class AStageKubeJSPlugin extends KubeJSPlugin {
    static {
        if (Mods.KUBEJS.isLoaded()) {
            KubeJSStageEventHandler.init();
        }
    }

//    @Override
//    public void registerClasses(@NotNull ScriptType type, @NotNull ClassFilter filter) {
//        if (type.isServer()) {
//            filter.allow(Attributes.class);
//        }
//    }


    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        super.registerClasses(type, filter);
    }

    @Override
    public void registerBindings(@NotNull BindingsEvent event) {
        if (!Mods.KUBEJS.isLoaded()) return;

        if (event.getType().isServer()) {
            event.add("AStages", AStagesKubeJSUtil.class);
            event.add("Attributes", Attributes.class);
            event.add("ItemAttributes", Attributes.Item.class);
            event.add("ScreenAttributes", Attributes.Screen.class);
            event.add("PetAttributes", Attributes.Pet.class);
            event.add("DimensionAttributes", Attributes.Dimension.class);
            event.add("StructureAttributes", Attributes.Structure.class);
        }
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
        ARestrictionManager.reloadAfterScripts();
    }

    @Override
    public void clearCaches() {
        // BEFORE SERVER SCRIPT RELOADING!
        ARestrictionManager.reloadBeforeScripts();
    }
}
