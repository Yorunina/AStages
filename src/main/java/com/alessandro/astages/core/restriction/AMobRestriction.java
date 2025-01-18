package com.alessandro.astages.core.restriction;

import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AMobRestriction extends ARestriction<AMobRestriction, EntityType<?>, EntityType<?>> {
    private final List<EntityType<?>> mobs = new ArrayList<>();

    public AMobRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.SPAWNER)
            .addAttribute(Attributes.MOB_SPAWNING)

            .addAttribute(Attributes.DIMENSION, true)
            .addAttribute(Attributes.REPLACE, true)

            .addAttribute(Attributes.Mob.JADE_MOB_MESSAGE);
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

    public List<EntityType<?>> getMobs() {
        return mobs;
    }

    @SuppressWarnings("unused")
    public AMobRestriction setDisableSpawner(boolean value) {
        set(Attributes.SPAWNER, !value);
        return this;
    }

    @SuppressWarnings("unused")
    public AMobRestriction setDimension(ResourceLocation value) {
        set(Attributes.DIMENSION, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AMobRestriction setReplacing(ResourceLocation value) {
        set(Attributes.DIMENSION, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AMobRestriction setEnableMobSpawning(boolean value) {
        set(Attributes.MOB_SPAWNING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AMobRestriction setJadeMobMessage(Supplier<Component> message) {
        set(Attributes.Mob.JADE_MOB_MESSAGE, message);
        return this;
    }
}
