package com.alessandro.astages.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.UUID;

public class BlockStage implements INBTSerializable<CompoundTag> {
    public static String OWNER_KEY = "owner";
    private UUID owner;

    public BlockStage(IAttachmentHolder iAttachmentHolder) { }

    public BlockStage(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        return saveNBTData();
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag tag) {
        loadNBTData(tag);
    }

    public CompoundTag saveNBTData() {
        CompoundTag nbt = new CompoundTag();

        if (owner != null) {
            nbt.putUUID("owner", owner);
        }

        return nbt;
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt != null) {
            owner = nbt.contains(OWNER_KEY) ? nbt.getUUID(OWNER_KEY) : null;
        }
    }
}
