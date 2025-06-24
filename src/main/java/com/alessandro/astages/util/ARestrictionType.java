package com.alessandro.astages.util;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.ByIdMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;

@MethodsReturnNonnullByDefault
public enum ARestrictionType {
    ITEM(0),
    MOB(1),
    DIMENSION(2),
    STRUCTURE(3),
    RECIPE(4),
    SCREEN(5),
    ORE(6),
    PET(7),
    ENCHANT(8),
    CROP(9),
    EFFECT(10),
    REGION(11);

    public String getLowerCased() {
        return toString().toLowerCase(Locale.ROOT);
    }

    public static final IntFunction<ARestrictionType> BY_ID =
            ByIdMap.continuous(
                    ARestrictionType::getId,
                    ARestrictionType.values(),
                    ByIdMap.OutOfBoundsStrategy.ZERO
            );

    private final int id;

    ARestrictionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static List<String> types() {
        var toReturn = new ArrayList<String>();

        for (var type : values()) {
            toReturn.add(type.getLowerCased());
        }

        return toReturn;
    }
}
