package com.alessandro.astages.store;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;

@ParametersAreNonnullByDefault
public class ConfigurableAttributeStore extends AttributeStore {
    private final Set<Attribute<?>> modifiedAttributes = new HashSet<>();

    @Override
    public <T> AttributeStore setAttribute(Attribute<T> attribute, T value) {
        modifiedAttributes.add(attribute);
        return super.setAttribute(attribute, value);
    }

    public Set<Attribute<?>> getModifiedAttributes() {
        return modifiedAttributes;
    }
}
