package com.alessandro.astages.capability;

import com.alessandro.astages.api.annotation.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.annotation.nullability.Nullable;
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
    public static Capability<BlockStage> BLOCK_STAGE = CapabilityManager.get(new CapabilityToken<>() { });

    private BlockStage blockStage = null;
    private final LazyOptional<BlockStage> optional = LazyOptional.of(this::getOrCreateBlockStage);

    private BlockStage getOrCreateBlockStage() {
        if (this.blockStage == null) {
            this.blockStage = new BlockStage();
        }

        return this.blockStage;
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
