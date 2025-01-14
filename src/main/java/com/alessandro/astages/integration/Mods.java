package com.alessandro.astages.integration;

import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Mods {
    JEI,
    KUBEJS,
    JADE;

    public boolean isLoaded() {
        return ModList.get().isLoaded(asId());
    }

    public @NotNull String asId() {
        return toString().toLowerCase(Locale.ROOT);
    }
}
