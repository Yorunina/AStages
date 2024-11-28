package com.alessandro.astages.core.client;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.core.ARecipeRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AClientManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AClientRecipeManager implements AClientManager {
    public Map<String, List<AClientRecipeRestriction>> restrictions = new HashMap<>();

//    public Map<String, List<AClientRecipeRestriction>> getAllUnlockedRestrictions() {
//        Map<String, List<AClientRecipeRestriction>> toReturn = new HashMap<>();
//
//        restrictions.forEach((stage, restrictions) -> {
//            if (!ClientPlayerStage.getPlayerStages().contains(stage)) {
//                toReturn.put(stage, restrictions);
//            }
//        });
//
//        return toReturn;
//    }

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
