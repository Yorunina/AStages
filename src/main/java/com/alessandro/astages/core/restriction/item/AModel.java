package com.alessandro.astages.core.restriction.item;

public class AModel<T> {
    private final T modelObject;

    public AModel(T modelObject) {
        this.modelObject = modelObject;
    }

    public T getModelObject() {
        return modelObject;
    }
}
