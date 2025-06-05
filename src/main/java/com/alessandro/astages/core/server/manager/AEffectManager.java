package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.AEffectRestriction;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import net.minecraft.world.effect.MobEffect;

public class AEffectManager extends AManager<AEffectRestriction, MobEffect, MobEffect> {
    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.EFFECT;
    }
}
