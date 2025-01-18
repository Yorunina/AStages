package com.alessandro.astages.core.client;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AClientManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AClientRecipeManager implements AClientManager {
    public final Map<String, List<AClientRecipeRestriction>> restrictions = new HashMap<>();
    // Used for JEI/REI recipe hiding: contains all recipes TO HIDE!
    public final Map<RecipeType<?>, List<AClientRecipeRestriction>> CACHE = new HashMap<>();

    public void reloadBeforeScripts() {
        restrictions.clear();
        CACHE.clear();
    }

    public void addRestriction(String stage, @NotNull AClientRecipeRestriction restriction) {
        if (restriction.type() == null) {
            AStages.LOGGER.error("Recipe type for restriction {} is null!", restriction.id());
        }

        var newList = restrictions.getOrDefault(stage, new ArrayList<>());
        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id(), restriction.id())); }
        newList.add(restriction);
        restrictions.put(stage, newList);
    }

    // Remember generating cache before calling those two methods below!
//    @SuppressWarnings("unchecked")
//    public List<ResourceLocation> hideAllRecipesForType(IJeiRuntime runtime, RecipeType<?> vanillaType, mezz.jei.api.recipe.RecipeType<?> jeiType) {
//        var allRecipes = (Stream<Recipe<?>>) runtime.getRecipeManager().createRecipeLookup(jeiType).includeHidden().get();
//
//        for (var rs : getAllResourceLocationForType(vanillaType)) {
//            var recipe = allRecipes.filter(r -> r.getId().equals(rs)).findFirst().orElse(null);
//        }
//
//
//    }

    public List<ResourceLocation> getAllResourceLocationForType(RecipeType<?> type) {
        var restrictions = getRestrictionForType(type);
        var toReturn = new ArrayList<ResourceLocation>();

        restrictions.forEach(r -> toReturn.addAll(r.recipes()));

        return toReturn;
    }

    public List<AClientRecipeRestriction> getRestrictionForType(RecipeType<?> type) {
        return CACHE.getOrDefault(type, new ArrayList<>());
    }

    public void generateCache() {
        CACHE.clear();

        for (var entry : restrictions.entrySet()) {
            if (!ClientPlayerStage.hasStage(entry.getKey())) {
                var restrictions = entry.getValue();

                for (var restriction : restrictions) {
                    var newList = CACHE.getOrDefault(restriction.type(), new ArrayList<>());
                    newList.add(restriction);
                    CACHE.put(restriction.type(), newList);
                }
            }
        }
    }
}
