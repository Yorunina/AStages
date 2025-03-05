package com.alessandro.astages.core.restriction;

import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.core.wrapper.RecipeOutputWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.recipe.RecipeSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestRecipeReloadS2CPacket;
import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.util.AMarkable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ARecipeRestriction extends ARestriction<ARecipeRestriction, RecipeWrapper, RecipeWrapper> implements AMarkable {
    private RecipeType<?> type = null;
    private Item output = null;
    private String modId = null;
    private final List<ResourceLocation> recipes = new ArrayList<>();

    private final RuntimeException TYPE_EXCEPTION = new RuntimeException("Trying to add recipes of different type for restriction with id: " + getId() + "!");

    public ARecipeRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder();
    }

    @Override
    public ARecipeRestriction restrict(RecipeWrapper wrapper) {
        if (type == null) {
            this.type = wrapper.type();
        }

        if (type == wrapper.type()) {
            this.recipes.add(wrapper.recipe());
        } else {
            throw TYPE_EXCEPTION;
        }

        return this;
    }

    public ARecipeRestriction restrict(RecipeOutputWrapper wrapper) {
        if (type == null) {
            this.type = wrapper.type();
        }

        if (type == wrapper.type()) {
            this.output = wrapper.output();
        } else {
            throw TYPE_EXCEPTION;
        }

        return this;
    }

    public ARecipeRestriction restrict(@NotNull RecipeModWrapper wrapper) {
        this.modId = wrapper.modId();
        return this;
    }

    @Override
    public boolean isRestricted(@NotNull RecipeWrapper wrapper) {
        if (type != wrapper.type()) {
            return false;
        }

        return recipes.contains(wrapper.recipe());
    }

    public boolean isRestricted(@NotNull RecipeOutputWrapper wrapper) {
        if (output == null) { return false; }
        if (type != wrapper.type()) { return false; }

        return output.equals(wrapper.output());
    }

    public boolean isRestricted(RecipeModWrapper wrapper) {
        if (modId == null) { return false; }

        return modId.equals(wrapper.modId());
    }

    public RecipeType<?> getType() {
        return type;
    }

    public List<ResourceLocation> getRecipes() {
        return recipes;
    }

    public String getModId() {
        return modId;
    }

    @Override
    public void markAsDirty() {
        if (type != null && !recipes.isEmpty()) {
            ModNetworking.sendToClients(new RecipeSyncerS2CPacket(getId(), getStage(), getPriority(), type, recipes));
        }

        ModNetworking.sendToClients(new RequestRecipeReloadS2CPacket());
    }
}
