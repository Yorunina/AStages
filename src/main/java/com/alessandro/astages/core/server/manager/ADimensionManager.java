package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.ADimensionRestriction;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ADimensionManager extends AManager<ADimensionRestriction, ResourceLocation, ResourceLocation> {
    public final OrderedMultiMap<ResourceLocation, ADimensionRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(ADimensionRestriction restriction) {
        super.addRestriction(restriction);

        for (var dimension : restriction.getDimensions()) {
            CACHE.put(dimension, restriction);
        }
    }

    @Override
    public ADimensionRestriction getRestriction(Player player, ResourceLocation dimension) {
        return getRestrictionFromCache(CACHE, dimension, player);
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.DIMENSION;
    }
}
