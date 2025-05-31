package com.alessandro.astages.core.restriction.recipe;

import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.recipe.RecipeSyncerS2CPacket;
import com.alessandro.astages.store.ReloadType;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.util.AMarkable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class ARecipeRestriction extends ABaseRecipeRestriction<ARecipeRestriction, RecipeWrapper, RecipeWrapper> implements AMarkable {
    private RecipeType<?> type = null;
    private final List<ResourceLocation> recipes = new ArrayList<>();

    private final RuntimeException TYPE_EXCEPTION = new RuntimeException("Trying to add recipes of different type for restriction with id: " + getId() + "!");

    public ARecipeRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public ARecipeRestriction restrict(RecipeWrapper wrapper) {
        if (type == null) { this.type = wrapper.type(); }

        if (type == wrapper.type()) {
            this.recipes.add(wrapper.recipe());
        } else {
            throw TYPE_EXCEPTION;
        }

        return this;
    }

    @Override
    public boolean isRestricted(RecipeWrapper wrapper) {
        return type == wrapper.type() && recipes.contains(wrapper.recipe());
    }

    public RecipeType<?> getType() {
        return type;
    }

    public List<ResourceLocation> getRecipes() {
        return recipes;
    }

    @Override
    public void markAsDirty() {
        if (type != null && !recipes.isEmpty()) {
            ModNetworking.sendToClients(new RecipeSyncerS2CPacket(getId(), getStage(), getPriority(), type, recipes));
        }

        // ModNetworking.sendToClients(new RequestRecipeReloadS2CPacket());
        ModNetworking.sendToClients(new RequestReloadS2CPacket(ReloadType.RECIPE));
    }
}
