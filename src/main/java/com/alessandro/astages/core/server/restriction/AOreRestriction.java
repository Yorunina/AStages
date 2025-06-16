package com.alessandro.astages.core.server.restriction;

import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.ore.OreSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.util.AMarkable;
import com.alessandro.astages.util.ReloadType;
import com.alessandro.astages.util.develop.UnderDevelopment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
    public AOreRestriction restrict(OreWrapper wrapper) {
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
        ModNetworking.sendToClients(new OreSyncerS2CPacket(getId(), getStage(), original, replacement));
        ModNetworking.sendToClients(new RequestReloadS2CPacket(ReloadType.ORE));
        // ARestrictionManager.synchronizeOreStages(null); // TODO
    }
}
