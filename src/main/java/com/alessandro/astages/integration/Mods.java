package com.alessandro.astages.integration;

import com.alessandro.astages.util.annotations.NotNullMethodsReturn;
import net.minecraftforge.fml.ModList;

import java.util.Locale;

@NotNullMethodsReturn
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
