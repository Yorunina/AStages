package com.alessandro.astages.util;

public interface ASendable<T extends ARestriction> {
    void sendToClientIfRestrictionChanged(T restriction);
}
