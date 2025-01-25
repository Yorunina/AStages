package com.alessandro.astages.store;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.OrderedMultiMap;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public abstract class AManager<R extends ARestriction<R, U, V>, U, V> {
    public final Map<String, List<R>> restrictions = new HashMap<>();

    public Map<String, List<R>> getRestrictions() {
        return restrictions;
    }

    public void reloadBeforeScripts() {
        restrictions.clear();
    }

    public R getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (R restriction : restrictions.get(stage)) {
                if (restriction.getId().equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public R getRestriction(Player player, V object) {
        for (String stage : restrictions.keySet()) {
            for (R restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(object) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Info("Could be a de-synchronized with caches")
    public void addRestriction(R restriction) {
        var newList = restrictions.getOrDefault(restriction.getStage(), new ArrayList<>());
        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.getId(), restriction.getId())); }

        newList.add(restriction);
        restrictions.put(restriction.getStage(), newList);

        ARestrictionManager.ALL_STAGES.add(restriction.getStage());
    }

    public <W> R getRestrictionFromCache(OrderedMultiMap<W, R> cache, W value, Player player) {
        var restrictions = cache.get(value);

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (!AStagesUtil.hasStage(player, restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
