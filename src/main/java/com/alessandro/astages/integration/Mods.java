package com.alessandro.astages.integration;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.fml.ModList;

import java.util.Locale;

@MethodsReturnNonnullByDefault
public enum Mods {
    JEI,
    KUBEJS,
    JADE;

    public boolean isLoaded() {
        return ModList.get().isLoaded(asId());
    }

    public String asId() {
        return toString().toLowerCase(Locale.ROOT);
    }
}
