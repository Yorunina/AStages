package com.alessandro.astages.simple;

import com.alessandro.astages.util.ARestrictionType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ParametersAreNonnullByDefault
public enum ASimpleRestrictionType {
    ITEM,
    MOD,
    DIMENSION,
    GUI,
    ORE,
    STRUCTURE,
    BIOME,
    TAME,
    MOUNT,
    RECIPE,
    ARMOR;

    public String getId() {
        return toString().toLowerCase(Locale.ROOT);
    }

    public static List<String> types() {
        var toReturn = new ArrayList<String>();

        for (var type : values()) {
            toReturn.add(type.getId());
        }

        return toReturn;
    }

    public ARestrictionType convert() {
        return switch (this) {
            case ITEM, MOD, ARMOR -> ARestrictionType.ITEM;
            case DIMENSION -> ARestrictionType.DIMENSION;
            case GUI -> ARestrictionType.SCREEN;
            case ORE -> ARestrictionType.ORE;
            case STRUCTURE -> ARestrictionType.STRUCTURE;
            case BIOME -> throw new IllegalArgumentException("Biome not yet implemented!");
            case TAME, MOUNT -> ARestrictionType.PET;
            case RECIPE -> ARestrictionType.RECIPE;
        };
    }
}
