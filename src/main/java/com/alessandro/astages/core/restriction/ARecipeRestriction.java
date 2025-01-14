package com.alessandro.astages.core.restriction;

import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.packet.syncer.JeiRecipeSyncerS2CPacket;
import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.util.AMarkable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ARecipeRestriction extends ARestriction<ARecipeRestriction, RecipeWrapper, RecipeWrapper> implements AMarkable {
    private RecipeType<?> type;
    private final List<ResourceLocation> recipes = new ArrayList<>();

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
            throw new RuntimeException("Trying to add recipes of different type for restriction with id: " + getId() + "!");
        }

        return this;
    }

    @Override
    public boolean isRestricted(@NotNull RecipeWrapper wrapper) {
        if (type != wrapper.type()) {
            return false;
        }

        return recipes.contains(wrapper.recipe());
    }

    public RecipeType<?> getType() {
        return type;
    }

    public List<ResourceLocation> getRecipes() {
        return recipes;
    }

    @Override
    public void markAsDirty() {
        PacketDistributor.sendToAllPlayers(new JeiRecipeSyncerS2CPacket(getId(), getStage(), BuiltInRegistries.RECIPE_TYPE.wrapAsHolder(type), recipes));
    }
}
