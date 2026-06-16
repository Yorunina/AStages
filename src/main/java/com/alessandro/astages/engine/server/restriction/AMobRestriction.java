package com.alessandro.astages.engine.server.restriction;

import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.api.wrapper.EquipmentWrapper;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.constant.AFilter;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@NotNullParamsAndMethodsReturn
public class AMobRestriction extends ARestriction<AMobRestriction, EntityType<?>, EntityType<?>> {
    private final List<EntityType<?>> mobs = new ArrayList<>();

    private final List<EquipmentWrapper> equipments = new ArrayList<>();
    private final List<MobSpawnType> disabledSpawnTypes = new ArrayList<>();
    private final List<ResourceLocation> restrictedBiomes = new ArrayList<>();

    public AMobRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.SPAWNER)
            .addAttribute(Attributes.MOB_SPAWNING)
            .addAttribute(Attributes.SPAWN_WITH_DIFFERENT_EQUIPMENT)
            .addAttribute(Attributes.ATTACKING).setAttribute(Attributes.ATTACKING, true) // Left click interactions
            .addAttribute(Attributes.RIGHT_CLICK_INTERACTIONS).setAttribute(Attributes.RIGHT_CLICK_INTERACTIONS, true)

            .addAttribute(Attributes.DIMENSION, true)
            .addAttribute(Attributes.REPLACE, true)
            .addAttribute(Attributes.MIN_LIGHT_LEVEL, true)
            .addAttribute(Attributes.MAX_LIGHT_LEVEL, true)

            .addAttribute(Attributes.Mob.JADE_MOB_MESSAGE)
            .addAttribute(Attributes.Mob.INTERACTION_MESSAGE)
            .addAttribute(Attributes.Mob.ATTACK_MESSAGE);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, AMobRestriction.class)
            .build();
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

    public AMobRestriction associateLootRestriction(String id) {
        var restriction = new ALootRestriction(id, getStage());
        for (var mob : mobs) { restriction.restrictForEntities(mob); }
        restriction.setEntityFilter(AFilter.ALL);
        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return this;
    }

    public AMobRestriction associateLootRestriction() {
        return associateLootRestriction(getId() + ALootRestriction.IDENTIFIER);
    }

    public List<EquipmentWrapper> getEquipments() {
        return equipments;
    }

    public List<MobSpawnType> getDisabledSpawnTypes() {
        return disabledSpawnTypes;
    }

    public List<ResourceLocation> getRestrictedBiomes() {
        return restrictedBiomes;
    }

    public AMobRestriction spawnWithEquipment() {
        set(Attributes.SPAWN_WITH_DIFFERENT_EQUIPMENT, true);
        return this;
    }

    public AMobRestriction equipment(EquipmentSlot slot, ItemStack stack) {
        equipments.add(new EquipmentWrapper(slot, stack));
        return this;
    }

    public AMobRestriction restrictSpawnType(MobSpawnType... types) {
        disabledSpawnTypes.addAll(List.of(types));
        return this;
    }

    public AMobRestriction minLightLevel(int value) {
        set(Attributes.MIN_LIGHT_LEVEL, value);
        return this;
    }

    public AMobRestriction maxLightLevel(int value) {
        set(Attributes.MAX_LIGHT_LEVEL, value);
        return this;
    }

    public AMobRestriction restrictBiomeSpawn(ResourceLocation biome) {
        restrictedBiomes.add(biome);
        return this;
    }

    public AMobRestriction dimension(ResourceLocation value) {
        set(Attributes.DIMENSION, value);
        return this;
    }

    public AMobRestriction replaceWith(EntityType<?> value) {
        set(Attributes.REPLACE, value);
        return this;
    }

    public AMobRestriction disableSpawning() {
        set(Attributes.MOB_SPAWNING, false);
        return this;
    }

    public AMobRestriction disableAttack() {
        set(Attributes.ATTACKING, false);
        return this;
    }

    public AMobRestriction disableRightClick() {
        set(Attributes.RIGHT_CLICK_INTERACTIONS, false);
        return this;
    }

    public AMobRestriction jadeMessage(Supplier<Component> message) {
        set(Attributes.Mob.JADE_MOB_MESSAGE, message);
        return this;
    }

    public AMobRestriction attackMessage(Supplier<Component> message) {
        set(Attributes.Mob.ATTACK_MESSAGE, message);
        return this;
    }

    public AMobRestriction interactionMessage(Supplier<Component> message) {
        set(Attributes.Mob.INTERACTION_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction spawnReplacementWithEquipment(boolean value) {
        set(Attributes.SPAWN_WITH_DIFFERENT_EQUIPMENT, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setEquipment(EquipmentSlot slot, ItemStack stack) {
        equipments.add(new EquipmentWrapper(slot, stack));
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setMinLightLevel(int value) {
        set(Attributes.MIN_LIGHT_LEVEL, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setMaxLightLevel(int value) {
        set(Attributes.MAX_LIGHT_LEVEL, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setDisableSpawner(boolean value) {
        set(Attributes.SPAWNER, !value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setDimension(ResourceLocation value) {
        set(Attributes.DIMENSION, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setReplacing(EntityType<?> value) {
        set(Attributes.REPLACE, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setEnableMobSpawning(boolean value) {
        set(Attributes.MOB_SPAWNING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setCanBeAttacked(boolean value) {
        set(Attributes.ATTACKING, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setCanBeRightClicked(boolean value) {
        set(Attributes.RIGHT_CLICK_INTERACTIONS, value);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setJadeMobMessage(Supplier<Component> message) {
        set(Attributes.Mob.JADE_MOB_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setAttackMessage(Supplier<Component> message) {
        set(Attributes.Mob.ATTACK_MESSAGE, message);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public AMobRestriction setInteractionMessage(Supplier<Component> message) {
        set(Attributes.Mob.INTERACTION_MESSAGE, message);
        return this;
    }
}
