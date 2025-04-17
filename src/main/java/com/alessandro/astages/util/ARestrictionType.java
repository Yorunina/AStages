package com.alessandro.astages.util;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ARestrictionType {
    ITEM,
    MOB,
    DIMENSION,
    STRUCTURE,
    RECIPE,
    SCREEN,
    ORE,
    PET,
    ENCHANT,
    CROP,
    EFFECT,
    REGION;

    public @NotNull String getId() {
        return toString().toLowerCase(Locale.ROOT);
    }
}
