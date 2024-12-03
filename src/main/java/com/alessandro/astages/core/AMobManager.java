package com.alessandro.astages.core;

import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class AMobManager implements AManager<AMobRestriction, EntityType<?>> {
    private final Map<String, List<AMobRestriction>> restrictions = new HashMap<>();

    @Override
    public void reloadBeforeScripts() {
        restrictions.clear();
    }

    @Override
    public void addRestriction(String stage, AMobRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public AMobRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (AMobRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public AMobRestriction getRestriction(Player player, EntityType<?> mob) {
        for (String stage : restrictions.keySet()) {
            for (AMobRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(mob) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
