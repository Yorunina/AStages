package com.alessandro.astages.core.client;

import java.util.ArrayList;

public class AClientRestrictionManager {
    private static int id = 0;

    public static final AClientItemManager ITEM_INSTANCE = new AClientItemManager();
    public static final AClientRecipeManager RECIPE_INSTANCE = new AClientRecipeManager();
    public static final AClientOreManager ORE_INSTANCE = new AClientOreManager();

    public static void onStageSync() {
        ITEM_INSTANCE.restrictedStacksForTooltip = new ArrayList<>();
        ITEM_INSTANCE.restrictedStacksForName = new ArrayList<>();
        ITEM_INSTANCE.notRestrictedStacksForTooltip = new ArrayList<>();
        ITEM_INSTANCE.notRestrictedStacksForName = new ArrayList<>();
    }

    public static int getId() {
        return id++;
    }
}
