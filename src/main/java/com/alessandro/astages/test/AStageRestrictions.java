package com.alessandro.astages.test;

public enum AStageRestrictions {
    PVP(false),
    BLOCK_BREAKING(false),
    BLOCK_INTERACTION(false),
    ENTITY_HURTING(false);

    public final boolean defaultValue;
    AStageRestrictions(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
}
