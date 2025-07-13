package com.alessandro.astages.core.server.restriction;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.ore.OreSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.store.*;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.util.ReloadType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AOreRestriction extends ARestriction<AOreRestriction, OreWrapper, BlockState> implements AChangeable, AMarkable {
    private BlockState original;
    private BlockState replacement;

    public AOreRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.AFFECTS_PLAYER_ACTIONS);
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
    public <T> AOreRestriction set(Attribute<T> attribute, T value) {
        var toReturn = super.set(attribute, value);

        if (attribute == Attributes.AFFECTS_PLAYER_ACTIONS) {
            setChanged();
        }

        return toReturn;
    }

    @Override
    public void setChanged() {
        ARestrictionManager.ORE_INSTANCE.recalculatePlayerActions(this);
    }

    @Override
    public void markAsDirty() {
        ModNetworking.sendToClients(new OreSyncerS2CPacket(getId(), getStage(), original, replacement));
        ModNetworking.sendToClients(new RequestReloadS2CPacket(ReloadType.ORE));
    }

    public AOreRestriction setAffectsPlayerActions(boolean value) {
        set(Attributes.AFFECTS_PLAYER_ACTIONS, value);
        return this;
    }
}
