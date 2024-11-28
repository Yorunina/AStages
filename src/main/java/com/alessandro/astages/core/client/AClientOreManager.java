package com.alessandro.astages.core.client;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AClientManager;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AClientOreManager implements AClientManager {
    public Map<String, List<AClientOreRestriction>> restrictions = new HashMap<>();

    public void addRestriction(String stage, @NotNull AClientOreRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id(), restriction.id())); }
        newList.add(restriction);

        restrictions.put(stage, newList);
    }

    public AClientOreRestriction getRestriction(BlockState original) {
        for (String stage : restrictions.keySet()) {
            for (AClientOreRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(original) && !ClientPlayerStage.hasStage(stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
