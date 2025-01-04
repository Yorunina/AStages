package com.alessandro.astages.store;

import org.jetbrains.annotations.NotNull;

public class SetAttributeNotSupported extends RuntimeException {
    public SetAttributeNotSupported(String message) {
        super(message);
    }

    public SetAttributeNotSupported(@NotNull Attribute<?> attribute) {
        super("Set method for restriction " + attribute.getId() + " is not supported for this object!");
    }
}
