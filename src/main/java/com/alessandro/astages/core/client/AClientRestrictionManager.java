package com.alessandro.astages.core.client;

import com.alessandro.astages.core.client.item.AClientItemManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AClientRestrictionManager {
    // public static final AClientItemManager ITEM_INSTANCE = new AClientItemManager();
    public static final AClientItemManager NEW_ITEM_INSTANCE = new AClientItemManager();
    public static final AClientRecipeManager RECIPE_INSTANCE = new AClientRecipeManager();
    public static final AClientOreManager ORE_INSTANCE = new AClientOreManager();
    public static final AClientMobManager MOB_INSTANCE = new AClientMobManager();

    public static final Set<String> ORE_STAGES = new HashSet<>();

    public static void reloadBeforeScripts() {
        // ITEM_INSTANCE.reloadBeforeScripts();
        NEW_ITEM_INSTANCE.reloadBeforeScripts();
        RECIPE_INSTANCE.reloadBeforeScripts();
        ORE_INSTANCE.reloadBeforeScripts();
        MOB_INSTANCE.reloadBeforeScripts();

        ORE_STAGES.clear();
    }

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
