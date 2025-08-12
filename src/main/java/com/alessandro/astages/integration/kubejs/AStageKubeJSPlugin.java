package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.stage.AStageManager;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.integration.kubejs.util.KubeJSStageEventHandler;
import com.alessandro.astages.integration.kubejs.util.StageEvents;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.ATime;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AStageKubeJSPlugin implements KubeJSPlugin {
    static {
        if (Mods.KUBEJS.isLoaded()) {
            KubeJSStageEventHandler.init();
        }
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        registry.register(ATime.class, (TypeWrapperRegistry.ContextFromFunction<ATime>) (context, object) -> ATime.of(object));
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        if (!Mods.KUBEJS.isLoaded()) return;

        if (bindings.type().isServer() || bindings.type().isStartup()) {
            bindings.add("AStages", AStagesKubeJSUtil.class);
        }

        if (bindings.type().isClient()) {
            bindings.add("AStagesClient", AStagesClientJSUtil.class);
        }

        bindings.add("AModels", AStagesModelJSUtil.class);
        bindings.add("Attributes", Attributes.class);
        bindings.add("ItemAttributes", Attributes.Item.class);
        bindings.add("ScreenAttributes", Attributes.Screen.class);
        bindings.add("PetAttributes", Attributes.Pet.class);
        bindings.add("DimensionAttributes", Attributes.Dimension.class);
        bindings.add("StructureAttributes", Attributes.Structure.class);
        bindings.add("ATime", ATime.class);
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
        AStageManager.reloadBeforeScripts();
        ARestrictionManager.reloadBeforeScripts();
    }

    @Override
    public void afterScriptsLoaded(ScriptManager manager) {
        AStageManager.reloadAfterScripts();
        ARestrictionManager.reloadAfterScripts();
    }
}
