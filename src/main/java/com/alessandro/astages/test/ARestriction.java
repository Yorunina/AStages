package com.alessandro.astages.test;

import java.util.List;

public abstract class ARestriction<T extends ARestriction<T>> {
    public final String id;
    public final String stage;

    public ARestriction(String id, String stage) {
        this.id = id;
        this.stage = stage;
    }


    public abstract boolean isDisabled(AStageRestrictions restriction) throws SetValueNotSupported;

    public abstract T setValue(AStageRestrictions restriction, boolean value) throws SetValueNotSupported;

    public abstract List<AStageRestrictions> allowedTypes();

    protected void checkProperty(AStageRestrictions restriction) throws SetValueNotSupported {
        if (!allowedTypes().contains(restriction)) {
            throw new SetValueNotSupported(restriction);
        }
    }
}
