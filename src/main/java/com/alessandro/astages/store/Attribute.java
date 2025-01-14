package com.alessandro.astages.store;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Attribute<T> {
    private final String id;
    private final Class<T> type;
    private final T defaultValue;

    private Attribute(String id, Class<T> type, @Nullable T defaultValue) {
        this.id = id;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    private Attribute(String id, AttributeType<T> attributeType, @Nullable T defaultValue) {
        this(id, attributeType.getType(), defaultValue);
    }

    public static <T> Attribute<T> create(String id, AttributeType<T> attributeType, @Nullable T defaultValue) {
        return new Attribute<>(id, attributeType, defaultValue);
    }

    public String getId() {
        return id;
    }

    public Class<T> getType() {
        return type;
    }

    public @Nullable T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "id='" + id + '\'' +
            ", type=" + type +
            ", defaultValue=" + defaultValue +
            '}';
    }
}
