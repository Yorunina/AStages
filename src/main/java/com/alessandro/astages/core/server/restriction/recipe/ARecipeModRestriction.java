package com.alessandro.astages.core.server.restriction.recipe;

import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.store.AMarkable;
import com.alessandro.astages.util.develop.UnderDevelopment;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ARecipeModRestriction extends ABaseRecipeRestriction<ARecipeModRestriction, RecipeModWrapper, RecipeWrapper> implements AMarkable {
    private String modId = null;

    public ARecipeModRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public ARecipeModRestriction restrict(RecipeModWrapper wrapper) {
        modId = wrapper.modId();
        return this;
    }

    @Override
    public boolean isRestricted(RecipeWrapper wrapper) {
        return modId.equals(wrapper.recipe().getNamespace());
    }

    public String getModId() {
        return modId;
    }

    @UnderDevelopment
    @Override
    public void markAsDirty() {

    }
}
