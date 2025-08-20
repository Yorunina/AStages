package com.alessandro.astages.store;

import com.alessandro.astages.util.annotations.NotNullParams;

@NotNullParams
public class SetAttributeNotSupported extends RuntimeException {
    public SetAttributeNotSupported(Attribute<?> attribute) {
        super("Set method for restriction " + attribute.getId() + " is not supported for this object!");
    }
}
