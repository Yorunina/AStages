package com.alessandro.astages.core.restriction.ud.models;

import net.minecraft.resources.ResourceLocation;

public abstract class ABaseModel {
    // <M extends ABaseModel<M>> {
    private final ResourceLocation id;

//    private final AttributeStore attributes;

    public ABaseModel(ResourceLocation id) {
        this.id = id;
//        this.attributes = allowedAttributes();
    }
//
//    public <T> T get(Attribute<T> attribute) {
//        checkAttribute(attribute);
//
//        return attributes.getAttribute(attribute);
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T> M set(Attribute<T> attribute, T value) {
//        checkAttribute(attribute);
//        attributes.setAttribute(attribute, value);
//
//        return (M) this;
//    }
//
//    public void checkAttribute(Attribute<?> attribute) throws SetAttributeNotSupported {
//        if (!allowedAttributes().containsKey(attribute)) {
//            throw new SetAttributeNotSupported(attribute);
//        }
//    }
//
//    public abstract @NotNull AttributeStore allowedAttributes();

    public ResourceLocation getId() {
        return id;
    }
}
