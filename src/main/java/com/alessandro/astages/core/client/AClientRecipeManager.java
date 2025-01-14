package com.alessandro.astages.core.client;

import com.alessandro.astages.AStages;
import com.alessandro.astages.util.AClientManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AClientRecipeManager implements AClientManager {
    public final Map<String, List<AClientRecipeRestriction>> restrictions = new HashMap<>();

    public void reloadBeforeScripts() {
        restrictions.clear();
    }

    public void addRestriction(String stage, @NotNull AClientRecipeRestriction restriction) {
        if (restriction.type() == null) {
            AStages.LOGGER.error("Recipe type for restriction {} is null!", restriction.id());
        }

        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id(), restriction.id())); }
        newList.add(restriction);

        restrictions.put(stage, newList);
    }
}
