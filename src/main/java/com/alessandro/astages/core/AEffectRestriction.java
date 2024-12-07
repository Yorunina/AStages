package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.world.effect.MobEffect;

import java.util.ArrayList;
import java.util.List;

public class AEffectRestriction implements ARestriction {
    public final String id;

    public List<MobEffect> effects = new ArrayList<>();

    public AEffectRestriction(String id) {
        this.id = id;
    }

    public AEffectRestriction restrict(MobEffect effect) {
        effects.add(effect);

        return this;
    }

    public boolean isRestricted(MobEffect effect) {
        return effects.contains(effect);
    }
}
