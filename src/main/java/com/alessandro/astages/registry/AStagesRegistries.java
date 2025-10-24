package com.alessandro.astages.registry;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.store.Attribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Collection;

@NotNullParamsAndMethodsReturn
public class AStagesRegistries {
    public static Collection<Attribute<?>> getAllEntries() {
        return AStages.ATTRIBUTES_REGISTRY.get().getValues();
    }

    public static class Keys {
        public static final ResourceKey<Registry<Attribute<?>>> ATTRIBUTES = ResourceKey.createRegistryKey(AResourceLocation.fromNamespaceAndPath("attributes"));
    }
}
