package com.alessandro.astages.core.restriction;

import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AMobRestriction extends ARestriction<AMobRestriction, EntityType<?>, EntityType<?>> {
    private final List<EntityType<?>> mobs = new ArrayList<>();

    public AMobRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.SPAWNER)

            .addAttribute(Attributes.DIMENSION, true)
            .addAttribute(Attributes.REPLACE, true);
    }

    @Override
    public AMobRestriction restrict(EntityType<?> mob) {
        mobs.add(mob);

        return this;
    }

    @Override
    public boolean isRestricted(EntityType<?> mob) {
        return mobs.contains(mob);
    }

    @SuppressWarnings("unused")
    public AMobRestriction setDisableSpawner(boolean value) {
        setAttribute(Attributes.SPAWNER, !value);
        return this;
    }

    @SuppressWarnings("unused")
    public AMobRestriction setDimension(ResourceLocation value) {
        setAttribute(Attributes.DIMENSION, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AMobRestriction setReplacing(ResourceLocation value) {
        setAttribute(Attributes.DIMENSION, value);
        return this;
    }
}
