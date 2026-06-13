package com.alessandro.astages.infrastructure.capability;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

@NotNullParamsAndMethodsReturn
public class BlockStageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<BlockOwner> BLOCK_STAGE = CapabilityManager.get(new CapabilityToken<>() { });

    private BlockOwner blockOwner = null;
    private final LazyOptional<BlockOwner> optional = LazyOptional.of(this::getOrCreateBlockStage);

    private BlockOwner getOrCreateBlockStage() {
        if (this.blockOwner == null) {
            this.blockOwner = new BlockOwner();
        }

        return this.blockOwner;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == BLOCK_STAGE) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        getOrCreateBlockStage().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrCreateBlockStage().loadNBTData(nbt);
    }
}
