package com.alessandro.astages.core.server.restriction.recipe;

import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.recipe.RecipeModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.store.AMarkable;
import com.alessandro.astages.util.ReloadType;

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

    @Override
    public void markAsDirty() {
        if (modId != null) {
            ModNetworking.sendToClients(new RecipeModSyncerS2CPacket(this));
        }

        ModNetworking.sendToClients(new RequestReloadS2CPacket(ReloadType.RECIPE));
    }
}
