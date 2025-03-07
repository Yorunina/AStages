package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.stage.AStageManager;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.integration.kubejs.util.KubeJSStageEventHandler;
import com.alessandro.astages.integration.kubejs.util.StageEvents;
import com.alessandro.astages.store.Attributes;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptManager;

public class AStageKubeJSPlugin implements KubeJSPlugin {
    static {
        if (Mods.KUBEJS.isLoaded()) {
            KubeJSStageEventHandler.init();
        }
    }

//    @Override
//    public void registerClasses(@NotNull ScriptType recipeType, @NotNull ClassFilter filter) {
//        if (recipeType.isServer()) {
//            filter.allow(Attributes.class);
//        }
//    }


//    @Override
//    public void registerClasses(ScriptType recipeType, ClassFilter filter) {
//        super.registerClasses(recipeType, filter);
//    }


    @Override
    public void registerBindings(BindingRegistry bindings) {
        if (!Mods.KUBEJS.isLoaded()) return;

        if (bindings.type().isServer()) {
            bindings.add("AStages", AStagesKubeJSUtil.class);
            bindings.add("Attributes", Attributes.class);
            bindings.add("ItemAttributes", Attributes.Item.class);
            bindings.add("ScreenAttributes", Attributes.Screen.class);
            bindings.add("PetAttributes", Attributes.Pet.class);
            bindings.add("DimensionAttributes", Attributes.Dimension.class);
            bindings.add("StructureAttributes", Attributes.Structure.class);
        }

        if (bindings.type().isClient()) {
            bindings.add("AStagesClient", AStagesClientJSUtil.class);
        }

        if (bindings.type().isClient() || bindings.type().isServer()) {
            bindings.add("AModels", AStagesModelJSUtil.class);
        }
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        if (!Mods.KUBEJS.isLoaded()) return;

        registry.register(StageEvents.GROUP);
    }

    @Override
    public void init() {
        if (!Mods.KUBEJS.isLoaded()) return;

        AStages.LOGGER.debug("ASTAGES-KUBEJS: INITIALIZED PLUGIN!");
    }

    @Override
    public void beforeScriptsLoaded(ScriptManager manager) {
        ARestrictionManager.reloadBeforeScripts();
        AStageManager.reloadBeforeScripts();
    }

    @Override
    public void afterScriptsLoaded(ScriptManager manager) {
        ARestrictionManager.reloadAfterScripts();
        AStageManager.reloadAfterScripts();
    }

//    @Override
//    public void onServerReload() {
//        // AFTER SERVER SCRIPT RELOADING!
//        ARestrictionManager.reloadAfterScripts();
//        AStageManager.reloadAfterScripts();
//    }
//
//    @Override
//    public void clearCaches() {
//        // BEFORE SERVER SCRIPT RELOADING!
//        ARestrictionManager.reloadBeforeScripts();
//        AStageManager.reloadBeforeScripts();
//    }
}
