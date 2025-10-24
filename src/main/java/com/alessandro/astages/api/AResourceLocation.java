package com.alessandro.astages.api;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import net.minecraft.resources.ResourceLocation;

@NotNullMethodsReturn
public class AResourceLocation {
    @SuppressWarnings("removal")
    public static ResourceLocation fromNamespaceAndPath(String path) {
        return new ResourceLocation(AStages.MODID, path);
    }

    @SuppressWarnings("removal")
    public static ResourceLocation parse(String location) {
        return new ResourceLocation(location);
    }
}
