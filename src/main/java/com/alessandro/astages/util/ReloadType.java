package com.alessandro.astages.util;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum ReloadType {
    CLIENT_BEFORE(0),
    RECIPE(1),
    ORE(2),
    ITEM(3);

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
