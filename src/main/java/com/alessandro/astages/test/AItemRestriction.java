package com.alessandro.astages.test;

import java.util.List;

public class AItemRestriction extends ARestriction<AItemRestriction> {
    public AItemRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public boolean isDisabled(AStageRestrictions restriction) throws SetValueNotSupported {
        checkProperty(restriction);

        return false;
    }

    @Override
    public AItemRestriction setValue(AStageRestrictions restriction, boolean value) throws SetValueNotSupported {
        checkProperty(restriction);

        return null;
    }

    @Override
    public List<AStageRestrictions> allowedTypes() {
        return List.of();
    }
}
