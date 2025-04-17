package com.alessandro.astages.core.manager;

import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.core.restriction.ARegionRestriction;
import com.alessandro.astages.store.AManager;
import com.alessandro.astages.store.ServerStageReadable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class ARegionManager extends AManager<ARegionRestriction, Void, BlockPos> implements ServerStageReadable<ARegionRestriction, BlockPos> {
    @Override
    public ARegionRestriction getRestriction(MinecraftServer server, BlockPos object) {
        var data = ServerStageData.getData(server);

        if (!getRestrictions().isEmpty()) {
            for (var restriction : getRestrictions()) {
                if (!data.has(restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public ARegionRestriction getRestriction(BlockPos blockPos, @Nullable Player player, @Nullable MinecraftServer server) {
        ARegionRestriction serverRestriction = null;
        ARegionRestriction playerRestriction = null;

        if (server != null) { serverRestriction = getRestriction(server, blockPos); }
        if (player != null) { playerRestriction = getRestriction(player, blockPos); }

        if (serverRestriction == null) { // If the stage is unlocked in the server, pass!
            return null;
        }

        return playerRestriction;
    }
}
