package com.alessandro.astages.store.client;

import com.alessandro.astages.util.ARestrictionType;

public interface AClientMinimalManager<R> {
    ARestrictionType associatedType();

    R getRestriction(String id);

    void removeRestriction(String id);
}
