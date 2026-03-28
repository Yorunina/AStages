package com.alessandro.astages.infrastructure.registry;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.store.ASimpleRestrictionType;
import com.alessandro.astages.api.store.Attribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;

@NotNullParamsAndMethodsReturn
public class AStagesRegistries {
    public static final IForgeRegistry<Attribute<?>> ATTRIBUTES = AStages.ATTRIBUTES_REGISTRY.get();
    public static final IForgeRegistry<ARestrictionType> RESTRICTION_TYPES = AStages.RESTRICTION_TYPES_REGISTRY.get();
    public static final IForgeRegistry<ASimpleRestrictionType> SIMPLE_RESTRICTION_TYPES = AStages.SIMPLE_RESTRICTION_TYPES_REGISTRY.get();

    public static class Keys {
        public static final ResourceKey<Registry<Attribute<?>>> ATTRIBUTES = ResourceKey.createRegistryKey(AResourceLocation.fromNamespaceAndPath("attributes"));
        public static final ResourceKey<Registry<ARestrictionType>> RESTRICTION_TYPES = ResourceKey.createRegistryKey(AResourceLocation.fromNamespaceAndPath("restriction_types"));
        public static final ResourceKey<Registry<ASimpleRestrictionType>> SIMPLE_RESTRICTION_TYPES = ResourceKey.createRegistryKey(AResourceLocation.fromNamespaceAndPath("simple_restriction_types"));
    }
}
