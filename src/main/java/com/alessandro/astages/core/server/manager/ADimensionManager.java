package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.ARestrictionUtils;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.server.restriction.ADimensionRestriction;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.server.AManager;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
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
    public ADimensionRestriction getRestriction(AHolder holder, ResourceLocation dimension) {
        // 有则代表有限制
        ADimensionRestriction serverRestriction = null;
        ADimensionRestriction playerRestriction = null;
        if (holder.isServerActive()) {
            serverRestriction = ARestrictionUtils.getRestrictionFromCache(holder, AStageType.SERVER, CACHE, dimension);
        }

        if (holder.isPlayerActive()) {
            playerRestriction = ARestrictionUtils.getRestrictionFromCache(holder, AStageType.PLAYER, CACHE, dimension);
        }
        ADimensionRestriction res = (ADimensionRestriction) ARestrictionUtils.getServerAndPlayerRestriction(serverRestriction, playerRestriction);
        return res;
    }


    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.DIMENSION;
    }
}
