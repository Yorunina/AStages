package com.alessandro.astages.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.UUID;

@AutoRegisterCapability
public class BlockStage {
    public static String OWNER_KEY = "owner";
    private UUID owner;

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (owner != null) {
            nbt.putUUID("owner", owner);
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt != null) {
            owner = nbt.contains(OWNER_KEY) ? nbt.getUUID(OWNER_KEY) : null;
        }
    }
}
