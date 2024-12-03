package com.alessandro.astages.core;

import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.ud.OreSyncerS2CPacket;
import com.alessandro.astages.util.AMarkable;
import com.alessandro.astages.util.ARestriction;
import net.minecraft.world.level.block.state.BlockState;

public class AOreRestriction implements ARestriction, AMarkable {
    public final String id;
    public final String stage;

    public BlockState original;
    public BlockState replacement;

    public AOreRestriction(String id, String stage) {
        this.id = id;
        this.stage = stage;
    }

    public AOreRestriction restrict(BlockState original, BlockState replacement) {
        this.replacement = replacement;
        this.original = original;

        return this;
    }

    public boolean isRestricted(BlockState original) {
        return this.original.equals(original);
    }

    @Override
    public void markAsDirty() {
        ModNetworking.sendToClients(new OreSyncerS2CPacket(id, stage, original, replacement, true));
        ARestrictionManager.synchronizeOreStages(null);
    }
}
