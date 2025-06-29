package com.alessandro.astages.util;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum ReloadType {
    CLIENT_BEFORE(0),
    CLIENT_SYNC(1),
    RELOAD_BEFORE(2),
    RECIPE(3),
    ORE(4),
    ITEM(5);

    public static final IntFunction<ReloadType> BY_ID =
        ByIdMap.continuous(
            ReloadType::getId,
            ReloadType.values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
        );

    private final int id;

    ReloadType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
