package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.AStructureRestriction;
import com.alessandro.astages.event.structure.ServerEventHandler;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AStructureManager extends AManager<AStructureRestriction, ResourceLocation, ResourceLocation> {
    public final OrderedMultiMap<ResourceLocation, AStructureRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        ServerEventHandler.playerIsInStructure.clear();
        CACHE.clear();
    }

    @Override
    public void addRestriction(AStructureRestriction restriction) {
        super.addRestriction(restriction);

        for (var structure : restriction.getStructures()) {
            CACHE.put(structure, restriction);
        }
    }

    @Override
    public AStructureRestriction getRestriction(Player player, ResourceLocation structure) {
        return getRestrictionFromCache(CACHE, structure, player);
    }
}
