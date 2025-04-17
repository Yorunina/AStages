package com.alessandro.astages.core.restriction;

import com.alessandro.astages.core.wrapper.EquipmentWrapper;
import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AMobRestriction extends ARestriction<AMobRestriction, EntityType<?>, EntityType<?>> {
    private final List<EntityType<?>> mobs = new ArrayList<>();

    private final List<EquipmentWrapper> equipments = new ArrayList<>();
    private final List<MobSpawnType> disabledSpawnTypes = new ArrayList<>();
    private final List<ResourceLocation> restrictedBiomes = new ArrayList<>();

    public AMobRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.SPAWNER)
            .addAttribute(Attributes.MOB_SPAWNING)
            .addAttribute(Attributes.SPAWN_WITH_DIFFERENT_EQUIPMENT)

            .addAttribute(Attributes.DIMENSION, true)
            .addAttribute(Attributes.REPLACE, true)
            .addAttribute(Attributes.MIN_LIGHT_LEVEL, true)
            .addAttribute(Attributes.MAX_LIGHT_LEVEL, true)

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
    public AMobRestriction spawnReplacementWithEquipment(boolean value) {
        set(Attributes.SPAWN_WITH_DIFFERENT_EQUIPMENT, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AMobRestriction setEquipment(EquipmentSlot slot, ItemStack stack) {
        equipments.add(new EquipmentWrapper(slot, stack));
        return this;
    }

    public List<EquipmentWrapper> getEquipments() {
        return equipments;
    }

    @SuppressWarnings("unused")
    public AMobRestriction restrictSpawnType(MobSpawnType... types) {
        disabledSpawnTypes.addAll(List.of(types));
        return this;
    }

    public List<MobSpawnType> getDisabledSpawnTypes() {
        return disabledSpawnTypes;
    }

    @SuppressWarnings("unused")
    public AMobRestriction setMinLightLevel(int value) {
        set(Attributes.MIN_LIGHT_LEVEL, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AMobRestriction setMaxLightLevel(int value) {
        set(Attributes.MAX_LIGHT_LEVEL, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AMobRestriction restrictBiomeSpawn(ResourceLocation biome) {
        restrictedBiomes.add(biome);
        return this;
    }

    public List<ResourceLocation> getRestrictedBiomes() {
        return restrictedBiomes;
    }

    @Deprecated(forRemoval = true)
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
    public AMobRestriction setReplacing(EntityType<?> value) {
        set(Attributes.REPLACE, value);
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
