package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.server.restriction.ARegionRestriction;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import net.minecraft.core.BlockPos;

@NotNullParams
public class ARegionManager extends AManager<ARegionRestriction, Void, BlockPos> {
    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.REGION;
    }
}
