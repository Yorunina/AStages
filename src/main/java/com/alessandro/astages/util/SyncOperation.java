package com.alessandro.astages.util;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum SyncOperation {
    ADD(0),
    REMOVE(1);

    public static final IntFunction<SyncOperation> BY_ID =
            ByIdMap.continuous(
                    SyncOperation::getId,
                    SyncOperation.values(),
                    ByIdMap.OutOfBoundsStrategy.ZERO
            );

    private final int id;

    SyncOperation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}