package com.alessandro.astages.core.client;

import com.alessandro.astages.util.AClientRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public record AClientRecipeRestriction(String id, String stage, RecipeType<?> type,
                                       List<ResourceLocation> recipes) implements AClientRestriction {
}
