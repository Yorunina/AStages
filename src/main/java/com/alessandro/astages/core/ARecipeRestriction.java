package com.alessandro.astages.core;

import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.ud.JeiRecipeSyncerS2CPacket;
import com.alessandro.astages.util.AMarkable;
import com.alessandro.astages.util.ARestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public class ARecipeRestriction implements ARestriction, AMarkable {
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
//        setChanged();

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
    public void markAsDirty() {
        ModNetworking.sendToClients(new JeiRecipeSyncerS2CPacket(id, stage, type, recipes));
    }

    //    @Override
//    public void setChanged() {
//        ARestrictionManager.RECIPE_INSTANCE.sendToClientIfRestrictionChanged(this);
//    }

    public RecipeType<?> getType() {
        return type;
    }

    public ARecipeRestriction setType(RecipeType<?> type) {
        this.type = type;
//        setChanged();

        return this;
    }
}
