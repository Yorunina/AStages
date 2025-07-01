package com.alessandro.astages.store;

import com.alessandro.astages.util.develop.Info;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

@ParametersAreNonnullByDefault
public class AttributeStore extends HashMap<Attribute<?>, Object> {
    @Contract(value = " -> new", pure = true)
    public static @NotNull AttributeStore builder() {
        return new AttributeStore();
    }

    public <T> AttributeStore addAttribute(Attribute<T> attribute) {
        return addAttribute(attribute, false);
    }

    @Info("If attribute is optional, allow NULL default value!")
    public <T> AttributeStore addAttribute(Attribute<T> attribute, boolean isOptional) {
        if (isOptional) {
            put(attribute, attribute.getDefaultValue());
        } else if (attribute.getDefaultValue() == null) {
            throw new NullPointerException(attribute.getId() + " has default value null!");
        } else {
            put(attribute, attribute.getDefaultValue());
        }

        return this;
    }

    @Info("Check if optional attribute has value or is null!")
    public <T> boolean isPresent(Attribute<T> attribute) {
        return get(attribute) == null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(Attribute<T> attribute) {
        return (T) get(attribute); // getOrDefault(attribute, attribute.getDefaultValue());
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> AttributeStore setAttribute(Attribute<T> attribute, T value) {
        put(attribute, value);

        return this;
    }

    public AttributeStore combineWith(AttributeStore store) {
        putAll(store);
        return this;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        for (var attribute : keySet()) {
            builder.append(attribute.getId()).append(" ");
        }

        return builder.toString();
    }
}
