package com.alessandro.astages.store.client;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.nullability.NotNullParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NotNullParams
public abstract class AClientManager<R extends AClientRestriction<R, U, V>, U, V> implements AClientMinimalManager<R> {
    private final List<R> restrictions = new ArrayList<>();
    private final Map<String, R> IDS = new HashMap<>();

    public List<R> getRestrictions() {
        return restrictions;
    }

    public void reloadBeforeScripts() {
        restrictions.clear();
        IDS.clear();
    }

    public void reloadAfterScripts() { }

    @Override
    public R getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public R getRestriction(V object) {
        return restrictions.stream().filter(r -> r.isRestricted(object) && !ClientPlayerStage.hasStage(r.getStage())).findFirst().orElse(null);
    }

    public void addRestriction(R restriction) {
        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);
    }

    public <W> R getRestrictionFromCache(OrderedMultiMap<W, R> cache, W value) {
        var restrictions = cache.get(value);

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (!ClientPlayerStage.hasStage(restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public <W> R getServerRestrictionFromCache(OrderedMultiMap<W, R> cache, W value) {
        var restrictions = cache.get(value);

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (!AClientRestrictionManager.SERVER_STAGES.contains(restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public void removeRestriction(String id) {
        IDS.remove(id);
        restrictions.removeIf(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return null;
    }
}
