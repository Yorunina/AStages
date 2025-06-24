package com.alessandro.astages.store.server;

import com.alessandro.astages.util.ARestrictionType;

public interface AMinimalManager<R> {
    ARestrictionType associatedType();

    R getRestriction(String id);

    void removeRestriction(String id);
}