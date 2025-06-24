package com.alessandro.astages.core;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.client.manager.AClientItemManager;
import com.alessandro.astages.core.client.manager.AClientMobManager;
import com.alessandro.astages.core.client.manager.AClientOreManager;
import com.alessandro.astages.core.client.manager.AClientRecipeManager;
import com.alessandro.astages.store.client.AClientMinimalManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.develop.Info;

import java.util.*;

public class AClientRestrictionManager {
    public static final Map<ARestrictionType, AClientMinimalManager<?>> ASSOCIATION_MAP = new HashMap<>();

    public static final AClientItemManager ITEM_INSTANCE = new AClientItemManager();
    public static final AClientRecipeManager RECIPE_INSTANCE = new AClientRecipeManager();
    public static final AClientOreManager ORE_INSTANCE = new AClientOreManager();
    public static final AClientMobManager MOB_INSTANCE = new AClientMobManager();

    public static final Set<String> ORE_STAGES = new HashSet<>();
    public static final Set<String> SERVER_STAGES = new HashSet<>();

    @Info("For automatic command completion")
    public static final Set<String> DIMENSION_IDS = new HashSet<>();

    public static boolean waitingForItemUpdate = false;
    public static boolean waitingForRecipeUpdate = false;

    public static boolean jeiIsReloading = false;

    static {
        ASSOCIATION_MAP.put(ITEM_INSTANCE.associatedType(), ITEM_INSTANCE);
        ASSOCIATION_MAP.put(RECIPE_INSTANCE.associatedType(), RECIPE_INSTANCE);
        ASSOCIATION_MAP.put(ORE_INSTANCE.associatedType(), ORE_INSTANCE);
        ASSOCIATION_MAP.put(MOB_INSTANCE.associatedType(), MOB_INSTANCE);
    }

    public static void reloadBeforeScripts() {
        setJeiIsReloading(false);

        ITEM_INSTANCE.reloadBeforeScripts();
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

    public static void setJeiIsReloading(boolean jeiIsReloading) {
        AClientRestrictionManager.jeiIsReloading = jeiIsReloading;
        AStages.LOGGER.debug("Jei Is Reloading: {}", jeiIsReloading);
    }

    public static void setWaitingForItemUpdate(boolean waitingForItemUpdate) {
        AClientRestrictionManager.waitingForItemUpdate = waitingForItemUpdate;
    }

    public static void setWaitingForRecipeUpdate(boolean waitingForRecipeUpdate) {
        AClientRestrictionManager.waitingForRecipeUpdate = waitingForRecipeUpdate;
        AStages.LOGGER.debug("Waiting For Recipe Update: {}", waitingForRecipeUpdate);
    }

    public static void removeRestriction(String id, ARestrictionType type) {
        ASSOCIATION_MAP.get(type).removeRestriction(id);
    }
}
