package com.alessandro.astages.store;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public abstract class AManager<R extends ARestriction<R, U, V>, U, V> {
    private final List<R> restrictions = new ArrayList<>();
    private final Map<String, R> IDS = new HashMap<>();

    public List<R> getRestrictions() {
        return restrictions;
    }

    public void reloadBeforeScripts() {
        restrictions.clear();
        IDS.clear();
    }

    public R getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public R getRestriction(Player player, V object) {
        return restrictions.stream().filter(r -> r.isRestricted(object) && !AStagesUtil.hasStage(player, r.getStage())).findFirst().orElse(null);
    }

    public void addRestriction(R restriction) {
        if (IDS.containsKey(restriction.getId())) {
            AStages.LOGGER.warn("Restriction with id {} already found!", restriction.getId());
            return;
        }

        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);
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
