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

@SuppressWarnings("removal")
@Deprecated(forRemoval = true)
@NotNullParamsAndMethodsReturn
public class PlayerStageProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerStage> PLAYER_STAGE = CapabilityManager.get(new CapabilityToken<>() { });

    private PlayerStage playerStage = null;
    private final LazyOptional<PlayerStage> optional = LazyOptional.of(this::getOrCreatePlayerStage);

    private PlayerStage getOrCreatePlayerStage() {
        if (this.playerStage == null) {
            this.playerStage = new PlayerStage();
        }

        return this.playerStage;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_STAGE) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        getOrCreatePlayerStage().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrCreatePlayerStage().loadNBTData(nbt);
    }
}
