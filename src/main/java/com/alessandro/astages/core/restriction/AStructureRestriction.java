package com.alessandro.astages.core.restriction;

import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AStructureRestriction extends ARestriction<AStructureRestriction, ResourceLocation, ResourceLocation> {
    private final List<ResourceLocation> structures = new ArrayList<>();
    private List<BlockState> allowedBreakableBlocks = null;
    private List<BlockState> allowedPlaceableBlocks = null;
    private List<BlockState> allowedInteractableBlocks = null;
    private List<EntityType<?>> allowedTargetableEntities = null;

    public AStructureRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.ATTACKING)
            .addAttribute(Attributes.GENERIC_INTERACTIONS)
            .addAttribute(Attributes.ENTERING)
            .addAttribute(Attributes.BLOCK_PLACING)
            .addAttribute(Attributes.BLOCK_BREAKING)
            .addAttribute(Attributes.EXPLOSIONS_AFFECT_BLOCKS)
            .addAttribute(Attributes.EXPLOSIONS_AFFECT_ENTITIES)

            .addAttribute(Attributes.Structure.ATTACK_MESSAGE)
            .addAttribute(Attributes.Structure.INTERACT_MESSAGE)
            .addAttribute(Attributes.Structure.ENTER_MESSAGE)
            .addAttribute(Attributes.Structure.PLACING_MESSAGE)
            .addAttribute(Attributes.Structure.MINING_MESSAGE);
    }

    @Override
    public AStructureRestriction restrict(ResourceLocation structure) {
        structures.add(structure);

        return this;
    }

    @Override
    public boolean isRestricted(ResourceLocation structure) {
        for (ResourceLocation str : structures) {
            if (str.equals(structure)) {
                return true;
            }
        }

        return false;
    }

    public List<ResourceLocation> getStructures() {
        return structures;
    }

    public boolean isBlockBreakable(BlockState state) {
        if (allowedBreakableBlocks == null) { return false; }
        return allowedBreakableBlocks.contains(state);
    }

    public boolean isBlockPlaceable(BlockState state) {
        if (allowedPlaceableBlocks == null) { return false; }
        return allowedPlaceableBlocks.contains(state);
    }

    public boolean isBlockInteractable(BlockState state) {
        if (allowedInteractableBlocks == null) { return false; }
        return allowedInteractableBlocks.contains(state);
    }

    public boolean isEntityTargetable(EntityType<?> entityType) {
        if (allowedTargetableEntities == null) { return false; }
        return allowedTargetableEntities.contains(entityType);
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setCanAttackEntities(boolean value) {
        set(Attributes.ATTACKING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setCanEnter(boolean value) {
        set(Attributes.ENTERING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setCanInteract(boolean value) {
        set(Attributes.GENERIC_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setCanBlockBePlaced(boolean value) {
        set(Attributes.BLOCK_PLACING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setCanBlockBeBroken(boolean value) {
        set(Attributes.BLOCK_BREAKING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setMakeExplosionsAffectBlocks(boolean value) {
        set(Attributes.EXPLOSIONS_AFFECT_BLOCKS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction setMakeExplosionsAffectEntities(boolean value) {
        set(Attributes.EXPLOSIONS_AFFECT_ENTITIES, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedBreakableBlocks(BlockState... states) {
        if (allowedBreakableBlocks == null) { allowedBreakableBlocks = new ArrayList<>(); }
        allowedBreakableBlocks.addAll(List.of(states));
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedPlaceableBlocks(BlockState... states) {
        if (allowedPlaceableBlocks == null) { allowedPlaceableBlocks = new ArrayList<>(); }
        allowedPlaceableBlocks.addAll(List.of(states));
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedInteractableBlocks(BlockState... states) {
        if (allowedInteractableBlocks == null) { allowedInteractableBlocks = new ArrayList<>(); }
        allowedInteractableBlocks.addAll(List.of(states));
        return this;
    }

    @SuppressWarnings("unused")
    public AStructureRestriction addAllowedTargetableEntities(EntityType<?>... entities) {
        if (allowedTargetableEntities == null) { allowedTargetableEntities = new ArrayList<>(); }
        allowedTargetableEntities.addAll(List.of(entities));
        return this;
    }
}
