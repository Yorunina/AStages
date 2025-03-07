package com.alessandro.astages.core.restriction;

import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ADimensionRestriction extends ARestriction<ADimensionRestriction, ResourceLocation, ResourceLocation> {
    private final List<ResourceLocation> dimensions = new ArrayList<>();

    public ADimensionRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.BIDIRECTIONAL)

            .addAttribute(Attributes.Dimension.ENTER_MESSAGE)
            .addAttribute(Attributes.Dimension.LEAVE_MESSAGE);
    }

    @Override
    public ADimensionRestriction restrict(ResourceLocation dimension) {
        dimensions.add(dimension);

        return this;
    }

    @Override
    public boolean isRestricted(ResourceLocation dimension) {
        for (ResourceLocation dim : dimensions) {
            if (dim.equals(dimension)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unused")
    public ADimensionRestriction setBidirectional(boolean value) {
        set(Attributes.BIDIRECTIONAL, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ADimensionRestriction setDimensionMessage(Function<ResourceLocation, Component> message) {
        set(Attributes.Dimension.ENTER_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ADimensionRestriction setLeaveDimensionMessage(Function<ResourceLocation, Component> message) {
        set(Attributes.Dimension.LEAVE_MESSAGE, message);
        return this;
    }
}
