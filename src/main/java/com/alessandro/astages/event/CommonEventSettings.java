package com.alessandro.astages.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonEventSettings {
    public static boolean isInventoryChanged = false;
    public static Integer slotChanged = null;

    public static final Map<UUID, Boolean> playersHaveOtherInventoriesOpened = new HashMap<>();
}
