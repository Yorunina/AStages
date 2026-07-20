package com.alessandro.astages.engine.client.restriction.recipe;

import com.alessandro.astages.api.nullability.NotNull;
import com.alessandro.astages.api.restriction.AClientRestriction;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;

public class AClientBaseRecipeRestriction<R extends AClientRestriction<R, U, V>, U, V> extends AClientRestriction<R, U, V> {
    public AClientBaseRecipeRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.REVERSE);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientBaseRecipeRestriction.class)
            .build();
    }

    @Override
    public R restrict(U object) {
        return null;
    }

    @Override
    public boolean isRestricted(V object) {
        return false;
    }
}
