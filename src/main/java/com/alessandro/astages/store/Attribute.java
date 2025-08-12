package com.alessandro.astages.store;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Attribute<T> {
    private static DeferredRegister<Attribute<?>> deferredRegister;

    private final String id;
    private final Class<T> type;
    private final T defaultValue;
    private T newDefaultValue;

    private Attribute(String id, Class<T> type, @Nullable T defaultValue) {
        this.id = id;
        this.type = type;
        this.defaultValue = defaultValue;
        newDefaultValue = null;
    }

    private Attribute(String id, AttributeType<T> attributeType, @Nullable T defaultValue) {
        this(id, attributeType.getType(), defaultValue);
    }

    public static DeferredRegister<Attribute<?>> setCurrentDeferredRegister(DeferredRegister<Attribute<?>> deferredRegister) {
        Attribute.deferredRegister = deferredRegister;
        return deferredRegister;
    }

    public static <T> Attribute<T> create(String id, AttributeType<T> attributeType, @Nullable T defaultValue) {
        var attribute = new Attribute<>(id, attributeType, defaultValue);
        deferredRegister.register(id, () -> attribute);
        return attribute;
    }

    @SuppressWarnings("unused")
    public void setNewDefaultValue(T newDefaultValue) {
        this.newDefaultValue = newDefaultValue;
    }

    public void resetDefaultValue() {
        this.newDefaultValue = null;
    }

    public String getId() {
        return id;
    }

    public Class<T> getType() {
        return type;
    }

    public @Nullable T getDefaultValue() {
        if (newDefaultValue != null) { return newDefaultValue; }
        return defaultValue;
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "id='" + id + '\'' +
            ", recipeType=" + type +
            ", defaultValue=" + defaultValue +
            '}';
    }
}
