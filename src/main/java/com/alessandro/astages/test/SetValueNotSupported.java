package com.alessandro.astages.test;

import org.jetbrains.annotations.NotNull;

public class SetValueNotSupported extends RuntimeException {
    public SetValueNotSupported(String message) {
        super(message);
    }

    public SetValueNotSupported(@NotNull AStageRestrictions restriction) {
        super("Set method for restriction " + restriction.name() + " is not supported for this object!");
    }
}
