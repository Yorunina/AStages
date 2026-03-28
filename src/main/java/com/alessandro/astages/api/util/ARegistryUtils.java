package com.alessandro.astages.api.util;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class ARegistryUtils {
    public static Set<String> getAllUniqueKeys(IForgeRegistry<?> registry) {
        Set<String> namespaces = new HashSet<>();

        for (var resourceLocation : registry.getKeys()) {
            namespaces.add(resourceLocation.getNamespace());
        }

        return namespaces;
    }
}
