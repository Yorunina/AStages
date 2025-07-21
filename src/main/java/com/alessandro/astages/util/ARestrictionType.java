package com.alessandro.astages.util;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
    REGION,
    LOOT;

    // TODO: Add ENCHANTMENTS!

    public String getId() {
        return toString().toLowerCase(Locale.ROOT);
    }

    public static List<String> types() {
        var toReturn = new ArrayList<String>();

        for (var type : values()) {
            toReturn.add(type.name().toLowerCase(Locale.ROOT));
        }

        return toReturn;
    }

    public static ARestrictionType getType(String name) {
        return ARestrictionType.valueOf(name.toUpperCase(Locale.ROOT));
    }
}
