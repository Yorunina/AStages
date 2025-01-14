package com.alessandro.astages.core.restriction;

import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import net.minecraft.world.effect.MobEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AEffectRestriction extends ARestriction<AEffectRestriction, MobEffect, MobEffect> {
    private final List<MobEffect> effects = new ArrayList<>();

    public AEffectRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder();
    }

    @Override
    public AEffectRestriction restrict(MobEffect effect) {
        effects.add(effect);

        return this;
    }

    @Override
    public boolean isRestricted(MobEffect effect) {
        return effects.contains(effect);
    }
}
