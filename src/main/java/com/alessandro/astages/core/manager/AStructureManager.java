package com.alessandro.astages.core.manager;

import com.alessandro.astages.core.restriction.AStructureRestriction;
import com.alessandro.astages.event.structure.ServerEventHandler;
import com.alessandro.astages.store.AManager;
import net.minecraft.resources.ResourceLocation;

public class AStructureManager extends AManager<AStructureRestriction, ResourceLocation, ResourceLocation> {
    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        ServerEventHandler.playerIsInStructure.clear();
    }
}
