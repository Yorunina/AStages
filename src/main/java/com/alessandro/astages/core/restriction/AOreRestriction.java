package com.alessandro.astages.core.restriction;

import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.packet.ore.OreSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.ReloadType;
import com.alessandro.astages.util.AMarkable;
import com.alessandro.astages.util.develop.UnderDevelopment;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class AOreRestriction extends ARestriction<AOreRestriction, OreWrapper, BlockState> implements AMarkable {
    private BlockState original;
    private BlockState replacement;

    public AOreRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder();
    }

    @Override
    public AOreRestriction restrict(@NotNull OreWrapper wrapper) {
        this.original = wrapper.original();
        this.replacement = wrapper.replacement();

        return this;
    }

    @Override
    public boolean isRestricted(BlockState original) {
        return this.original.equals(original);
    }

    public BlockState getOriginal() {
        return original;
    }

    public BlockState getReplacement() {
        return replacement;
    }

    @Override
    @UnderDevelopment
    public void markAsDirty() {
        PacketDistributor.sendToAllPlayers(new OreSyncerS2CPacket(getId(), getStage(), original, replacement));
        PacketDistributor.sendToAllPlayers(new RequestReloadS2CPacket(ReloadType.ORE));
        // ARestrictionManager.synchronizeOreStages(null); // TODO
    }
}
