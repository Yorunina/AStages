package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.core.server.restriction.ARegionRestriction;
import com.alessandro.astages.store.ServerStageReadable;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.api.annotation.nullability.NotNullParams;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;

@NotNullParams
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

//    @Override
//    public ARegionRestriction getRestriction(BlockPos blockPos, @Nullable Player player, @Nullable MinecraftServer server) {
//        ARegionRestriction serverRestriction = null;
//        ARegionRestriction playerRestriction = null;
//
//        if (server != null) { serverRestriction = getRestriction(server, blockPos); }
//        if (player != null) { playerRestriction = getRestriction(player, blockPos); }
//
//        if (serverRestriction == null) { // If the stage is unlocked in the server, pass!
//            return null;
//        }
//
//        return playerRestriction;
//    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.REGION;
    }
}
