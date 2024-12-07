package com.alessandro.astages.core;

import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class ACropManager implements AManager<ACropRestriction, ACropManager.CropWrapper> {
    public static final Map<String, List<ACropRestriction>> restrictions = new HashMap<>();

    @Override
    public void addRestriction(String stage, ACropRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public ACropRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (ACropRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public ACropRestriction getRestriction(Player player, ACropManager.CropWrapper wrapper) {
        for (String stage : restrictions.keySet()) {
            for (ACropRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(wrapper.crop, wrapper.age) && !AStagesUtil.hasStage(player, stage)) {
//                    if (restriction.isCropClass() && elaborateRestriction(restriction, wrapper.age)) {
//                        return restriction;
//                    }

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

    public record CropWrapper(BlockState crop, Integer age) { }
}
