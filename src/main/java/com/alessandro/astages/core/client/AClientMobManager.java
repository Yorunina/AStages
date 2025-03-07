package com.alessandro.astages.core.client;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AClientManager;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AClientMobManager implements AClientManager {
    public final Map<String, List<AClientMobRestriction>> restrictions = new HashMap<>();

    public void reloadBeforeScripts() {
        restrictions.clear();
    }

    public void addRestriction(String stage, @NotNull AClientMobRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id(), restriction.id())); }
        newList.add(restriction);

        restrictions.put(stage, newList);
    }

    public AClientMobRestriction getRestriction(EntityType<?> type) {
        for (String stage : restrictions.keySet()) {
            for (AClientMobRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(type) && !ClientPlayerStage.hasStage(stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
