package com.alessandro.astages.core.client;

import com.alessandro.astages.util.AClientRestriction;
import net.minecraft.world.level.block.state.BlockState;

public record AClientOreRestriction(String id, String stage, BlockState original,
                                    BlockState replacement) implements AClientRestriction {
    public boolean isRestricted(BlockState original) {
        return this.original.equals(original);
    }
}
