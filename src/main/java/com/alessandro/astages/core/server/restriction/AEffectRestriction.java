package com.alessandro.astages.core.server.restriction;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.item.ABaseItemRestriction;
import com.alessandro.astages.store.server.ARestriction;
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
        var defaultAttributes = AttributeStore.builder();

        var pluginAttributes = ARestrictionManager.ATTACHED_ATTRIBUTES.getOrDefault(AEffectRestriction.class, null);

        if (pluginAttributes != null) {
            return defaultAttributes.combineWith(pluginAttributes);
        } else {
            return defaultAttributes;
        }
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
