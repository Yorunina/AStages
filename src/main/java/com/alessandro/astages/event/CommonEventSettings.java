package com.alessandro.astages.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonEventSettings {
    private static boolean isInventoryChanged = false;
    private static Integer slotChanged = null;

    public static final Map<UUID, Boolean> playersHaveOtherInventoriesOpened = new HashMap<>();

    public static void allInventoryChanged() {
        isInventoryChanged = true;
        slotChanged = null;
    }

    public static void slotChanged(int slot) {
        isInventoryChanged = true;
        slotChanged = slot;
    }

    public static void resetSlotChanged() {
        isInventoryChanged = false;
    }

    public static Integer getSlotChanged() {
        return slotChanged;
    }

    public static boolean requireSlotCheck() {
        return isInventoryChanged;
    }
}
