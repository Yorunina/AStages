package com.alessandro.astages.core;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class APetManager implements AManager<APetRestriction, EntityType<?>> {
    private final Map<String, List<APetRestriction>> restrictions = new HashMap<>();

    @Override
    public void addRestriction(String stage, APetRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public APetRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (APetRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public APetRestriction getRestriction(EntityType<?> pet) {
        for (String stage : restrictions.keySet()) {
            for (APetRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(pet) && !ClientPlayerStage.getPlayerStages().contains(stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public APetRestriction getRestriction(Player player, EntityType<?> pet) {
        for (String stage : restrictions.keySet()) {
            for (APetRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(pet) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
