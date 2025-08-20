package com.alessandro.astages.core.client.manager;

import com.alessandro.astages.core.client.restriction.AClientMobRestriction;
import com.alessandro.astages.store.client.AClientManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.OrderedMultiMap;
import com.alessandro.astages.util.annotations.NotNullParams;
import net.minecraft.world.entity.EntityType;

@NotNullParams
public class AClientMobManager extends AClientManager<AClientMobRestriction, EntityType<?>, EntityType<?>> {
    private final OrderedMultiMap<EntityType<?>, AClientMobRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(AClientMobRestriction restriction) {
        super.addRestriction(restriction);

        for (var type : restriction.getMobs()) {
            CACHE.put(type, restriction);
        }
    }

    @Override
    public AClientMobRestriction getRestriction(EntityType<?> type) {
        var serverRestriction = getServerRestrictionFromCache(CACHE, type);
        if (serverRestriction == null) { return null; }

        return getRestrictionFromCache(CACHE, type);
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.MOB;
    }
}
