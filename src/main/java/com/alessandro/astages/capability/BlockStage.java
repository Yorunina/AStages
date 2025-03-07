package com.alessandro.astages.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
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
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return saveNBTData();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, @Nullable CompoundTag tag) {
        loadNBTData(tag);
    }

    public CompoundTag saveNBTData() {
        CompoundTag nbt = new CompoundTag();

        if (owner != null) {
            nbt.putUUID("owner", owner);
        }

        return nbt;
    }

    public void loadNBTData(@Nullable CompoundTag nbt) {
        if (nbt != null) {
            owner = nbt.contains(OWNER_KEY) ? nbt.getUUID(OWNER_KEY) : null;
        }
    }
}
