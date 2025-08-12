package com.alessandro.astages.core.server.restriction.recipe;

import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.packet.recipe.RecipeSyncerS2CPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ARecipeRestriction extends ABaseRecipeRestriction<ARecipeRestriction, RecipeWrapper, RecipeWrapper> {
    private RecipeType<?> type = null;
    private final List<ResourceLocation> recipes = new ArrayList<>();

    private final RuntimeException TYPE_EXCEPTION = new RuntimeException("Trying to add recipes of different type for restriction with id: " + getId() + "!");

    public ARecipeRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static ARecipeRestriction newBuilder() {
        return new ARecipeRestriction("null", "null");
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
            PacketDistributor.sendToAllPlayers(new RecipeSyncerS2CPacket(this));
        }

        super.markAsDirty();
    }
}
