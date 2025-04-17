package com.alessandro.astages.util.base;

public record Twin<A, B>(A a, B b) {
    public Twin() {
        this(null, null);
    }

    public boolean isValid() {
        return a != null && b != null;
    }
}
