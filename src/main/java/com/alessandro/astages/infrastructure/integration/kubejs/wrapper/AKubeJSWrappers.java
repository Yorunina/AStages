package com.alessandro.astages.infrastructure.integration.kubejs.wrapper;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.Nullable;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.Context;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class AKubeJSWrappers {
    public static @Nullable EntityType<?> wrapEntityType(Context context, @Nullable Object object) {
        if (object == null) { return null; }

        if (object instanceof EntityType<?> entityType) {
            return entityType;
        } else if (object instanceof CharSequence cs) {
            var s = cs.toString().trim();
            if (s.isEmpty() || s.equals("-") || s.equals("pig") || s.equals("minecraft:pig")) {
                return null;
            }

            var rs = AResourceLocation.parse(s);
            if (!ForgeRegistries.ENTITY_TYPES.containsKey(rs)) {
                ConsoleJS.getCurrent(context).error("Failed to read entity type from %s: Entity type with ID %s does not exist!".formatted(cs, cs));
            }

            return ForgeRegistries.ENTITY_TYPES.getValue(rs);
        }

        return null;
    }
}