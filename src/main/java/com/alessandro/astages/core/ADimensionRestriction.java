package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ADimensionRestriction implements ARestriction {
    public final String id;

    public boolean bidirectional = false; // NOT YET IMPLEMENTED!
    public Function<ResourceLocation, Component> dimensionMessage = resourceLocation -> Component.translatable("message.astages.dimension", resourceLocation).withStyle(ChatFormatting.RED);

    public List<ResourceLocation> dimensions = new ArrayList<>();

    public ADimensionRestriction(String id) {
        this.id = id;
    }

    public ADimensionRestriction restrict(ResourceLocation dimension) {
        dimensions.add(dimension);

        return this;
    }

    public boolean isRestricted(ResourceLocation dimension) {
        for (ResourceLocation dim : dimensions) {
            if (dim.equals(dimension)) {
                return true;
            }
        }

        return false;
    }

    public boolean isBidirectional() {
        return bidirectional;
    }

    public ADimensionRestriction setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;

        return this;
    }

    public Component getDimensionMessage(ResourceLocation resourceLocation) {
        return dimensionMessage.apply(resourceLocation);
    }

    public ADimensionRestriction setDimensionMessage(Function<ResourceLocation, Component> dimensionMessage) {
        this.dimensionMessage = dimensionMessage;

        return this;
    }
}
