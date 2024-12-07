package com.alessandro.astages.core;

import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class AEffectManager implements AManager<AEffectRestriction, MobEffect> {
    private final Map<String, List<AEffectRestriction>> restrictions = new HashMap<>();

    public Map<String, List<AEffectRestriction>> getRestrictions() {
        return restrictions;
    }

    @Override
    public void addRestriction(String stage, AEffectRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public AEffectRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (AEffectRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public AEffectRestriction getRestriction(Player player, MobEffect effect) {
        for (String stage : restrictions.keySet()) {
            for (AEffectRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(effect) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public void reloadBeforeScripts() {
        restrictions.clear();
    }
}
