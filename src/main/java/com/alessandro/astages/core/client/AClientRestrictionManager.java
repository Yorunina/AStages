package com.alessandro.astages.core.client;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AClientRestrictionManager {
    public static final AClientItemManager ITEM_INSTANCE = new AClientItemManager();
    public static final AClientRecipeManager RECIPE_INSTANCE = new AClientRecipeManager();
    public static final AClientOreManager ORE_INSTANCE = new AClientOreManager();

    public static final Set<String> ORE_STAGES = new HashSet<>();

    public static boolean isOreStage(String stage) {
        return ORE_STAGES.contains(stage);
    }

    public static boolean areOreStages(List<String> stages) {
        for (String stage : ORE_STAGES) {
            if (stages.contains(stage)) {
                return true;
            }
        }

        return false;
    }
}
