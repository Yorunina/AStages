package com.alessandro.astages.core.client.recipe;

import com.alessandro.astages.util.AClientRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record AClientRecipeRestriction(String id, String stage, int priority, RecipeType<?> type,
                                       List<ResourceLocation> recipes) implements AClientRestriction, Comparable<AClientRecipeRestriction>, AClientBaseRecipeRestriction {
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AClientRecipeRestriction)) { return false; }

        return this.id.equals(((AClientRecipeRestriction) obj).id) &&
            this.stage.equals(((AClientRecipeRestriction) obj).stage);
    }

    @Override
    public int compareTo(@NotNull AClientRecipeRestriction that) {
        if (this.priority == that.priority) {
            return this.id.compareTo(that.id);
        }

        return -Integer.compare(this.priority, that.priority);
    }
}
