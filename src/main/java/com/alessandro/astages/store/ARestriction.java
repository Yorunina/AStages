package com.alessandro.astages.store;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class ARestriction<R extends ARestriction<R, U, V>, U, V> {
    private final String id;
    private final String stage;

    private final AttributeStore attributes;

    public ARestriction(String id, String stage) {
        this.id = id;
        this.stage = stage;
        this.attributes = allowedAttributes();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isValueNull(Attribute<?> attribute) {
        return get(attribute) == null;
    }

    public <T> T get(Attribute<T> attribute) {
        checkAttribute(attribute);

        return attributes.getAttribute(attribute);
    }

    public <T> Component getMessage(Attribute<Function<T, Component>> attribute, T value) {
        var message = attributes.getAttribute(attribute);

        return message.apply(value);
    }

    public <T> void displayMessage(Attribute<Function<T, Component>> attribute, T value, Player player) {
        if (!isValueNull(attribute)) {
            player.displayClientMessage(getMessage(attribute, value), true);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> R set(Attribute<T> attribute, T value) {
        checkAttribute(attribute);
        attributes.setAttribute(attribute, value);

        return (R) this;
    }

    public boolean isDisabled(Attribute<Boolean> attribute) throws SetAttributeNotSupported {
        return !get(attribute);
    }

    public boolean isEnabled(Attribute<Boolean> attribute) throws SetAttributeNotSupported {
        return get(attribute);
    }

    public void checkAttribute(Attribute<?> attribute) throws SetAttributeNotSupported {
        if (!allowedAttributes().containsKey(attribute)) {
            throw new SetAttributeNotSupported(attribute);
        }
    }

    public abstract @NotNull AttributeStore allowedAttributes();

    public abstract R restrict(U object);

    public abstract boolean isRestricted(V object);

    public String getId() {
        return id;
    }

    public String getStage() {
        return stage;
    }
}
