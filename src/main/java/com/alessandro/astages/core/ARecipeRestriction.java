package com.alessandro.astages.core;

import com.alessandro.astages.util.AChangeable;
import com.alessandro.astages.util.ARestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public class ARecipeRestriction implements ARestriction, AChangeable {
    public String id;
    public String stage;

    public RecipeType<?> type;
    public List<ResourceLocation> recipes = new ArrayList<>();

    public ARecipeRestriction(String id, String stage) {
        this.id = id;
        this.stage = stage;
    }

    public ARecipeRestriction restrict(ResourceLocation recipe) {
        recipes.add(recipe);
        setChanged();

        return this;
    }

    public boolean isRestricted(RecipeType<?> type, ResourceLocation recipe) {
        if (this.type != type) { return false; }

        for (ResourceLocation rec : recipes) {
            if (rec.equals(recipe)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void setChanged() {
        ARestrictionManager.RECIPE_INSTANCE.sendToClientIfRestrictionChanged(this);
    }

    public RecipeType<?> getType() {
        return type;
    }

    public ARecipeRestriction setType(RecipeType<?> type) {
        this.type = type;
        setChanged();

        return this;
    }
}
