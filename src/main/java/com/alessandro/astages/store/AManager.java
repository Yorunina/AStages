package com.alessandro.astages.store;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
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

    public void addRestriction(R restriction) {
        var newList = restrictions.getOrDefault(restriction.getStage(), new ArrayList<>());
        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.getId(), restriction.getId())); }

        newList.add(restriction);
        restrictions.put(restriction.getStage(), newList);

        ARestrictionManager.ALL_STAGES.add(restriction.getStage());
    }
}
