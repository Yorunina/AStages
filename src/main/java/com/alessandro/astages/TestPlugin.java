package com.alessandro.astages;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.manager.ARegionManager;
import com.alessandro.astages.plugin.AStagesPlugin;
import com.alessandro.astages.plugin.ManagerContainer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TestPlugin implements AStagesPlugin {
    @Override
    public void reloadBeforeScripts() {
        AStages.LOGGER.debug("BEFORE SCRIPT!");
        AStages.LOGGER.debug(LOOT_INSTANCE().getIds().toString());
    }

    @Override
    public void registerManagers(ManagerContainer container) {
        container.register("loot", new ARegionManager());
    }

    @Override
    public ResourceLocation id() {
        return new ResourceLocation("astages:test_plugin");
    }

    public ARegionManager LOOT_INSTANCE() {
        return (ARegionManager) ARestrictionManager.getInstance("loot");
    }

//    public enum Manager {
//        LOOT_INSTANCE((ARegionManager) ARestrictionManager.getInstance("loot"));
//
//        Manager(ARegionManager loot) {
//
//        }
//    }
}
