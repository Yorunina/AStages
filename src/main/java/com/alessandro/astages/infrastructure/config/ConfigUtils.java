package com.alessandro.astages.infrastructure.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;

public class ConfigUtils {
    public static final Predicate<Object> ENTITY_TYPE_VALIDATOR = obj -> {
        if (obj instanceof String s) {
            ResourceLocation rl = ResourceLocation.tryParse(s.toLowerCase());
            if (rl != null) {
                return ForgeRegistries.ENTITY_TYPES.containsKey(rl);
            }
        }
        return false;
    };

    public static final Predicate<Object> SPAWN_TYPE_VALIDATOR = obj -> {
        if (obj instanceof String s) {
            try {
                MobSpawnType.valueOf(s.toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    };
}