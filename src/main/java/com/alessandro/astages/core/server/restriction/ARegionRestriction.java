package com.alessandro.astages.core.server.restriction;

import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.develop.Info;
import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ARegionRestriction extends ARestriction<ARegionRestriction, Void, BlockPos> {
    private Type type = Type.CUBE;

    private BlockPos center;
    private int deltaX;
    private int deltaY;
    private int deltaZ;

    public ARegionRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.GENERIC_INTERACTIONS)
            .addAttribute(Attributes.EXPLOSIONS_AFFECT_BLOCKS)
            .addAttribute(Attributes.EXPLOSIONS_AFFECT_ENTITIES)
            .addAttribute(Attributes.MOB_SPAWNING)

            .addAttribute(Attributes.DIMENSION, true)

            .addAttribute(Attributes.Region.INTERACT_MESSAGE);
    }

    @Override
    @DoNotCall
    @Info("Prefer using methods below!")
    public ARegionRestriction restrict(Void unused) { return null; }

    public ARegionRestriction setDimension(ResourceLocation dimension) {
        set(Attributes.DIMENSION, dimension);
        return this;
    }

    @Override
    public boolean isRestricted(@NotNull BlockPos blockPos) {
        if (type == Type.CUBE) {
            int dX = Math.abs(blockPos.getX() - center.getX());
            int dY = Math.abs(blockPos.getY() - center.getY());
            int dZ = Math.abs(blockPos.getZ() - center.getZ());
            return dX <= deltaX && dY <= deltaY && dZ <= deltaZ;
        }

        if (type == Type.SPHERE) {
            int dX = Math.abs(blockPos.getX() - center.getX());
            int dY = Math.abs(blockPos.getY() - center.getY());
            int dZ = Math.abs(blockPos.getZ() - center.getZ());
            return (dX * dX) / (deltaX * deltaX) + (dY * dY) / (deltaY * deltaY) + (dZ * dZ) / (deltaZ * deltaZ) <= 1;
        }

        return false;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ARegionRestriction setArea(Type type, BlockPos center, int deltaX, int deltaY, int deltaZ) {
        this.type = type;
        this.center = center;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;

        return this;
    }

    public ARegionRestriction setArea(Type type, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.type = type;
        this.deltaX = maxX - minX;
        this.deltaY = maxY - minY;
        this.deltaZ = maxZ - minZ;
        this.center = new BlockPos((maxX - deltaX) / 2, (maxY - deltaY) / 2, (maxZ - deltaZ) / 2);

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ARegionRestriction setArea(Type type, BlockPos center, int radius) {
        this.type = type;
        this.center = center;
        this.deltaX = radius;
        this.deltaY = radius;
        this.deltaZ = radius;

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ARegionRestriction setArea(Type type, @NotNull BlockPos from, @NotNull BlockPos to) {
        return setArea(type, from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ());
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setMakeExplosionsAffectBlocks(boolean value) {
        set(Attributes.EXPLOSIONS_AFFECT_BLOCKS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setMakeExplosionsAffectEntities(boolean value) {
        set(Attributes.EXPLOSIONS_AFFECT_ENTITIES, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setCanInteract(boolean value) {
        set(Attributes.GENERIC_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setEnableMobSpawning(boolean value) {
        set(Attributes.GENERIC_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ARegionRestriction setInteractMessage(Supplier<Component> message) {
        set(Attributes.Region.INTERACT_MESSAGE, message);
        return this;
    }

    public enum Type {
        SPHERE, CUBE
    }
}
