package com.alessandro.astages.core.client;

import com.alessandro.astages.AStages;
import com.alessandro.astages.util.AClientManager;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AClientRecipeManager implements AClientManager {
    public final Map<String, List<AClientRecipeRestriction>> restrictions = new HashMap<>();
    // Used for JEI/REI recipe hiding: contains all recipes TO HIDE!
    public final OrderedMultiMap<RecipeType<?>, AClientRecipeRestriction> CACHE = OrderedMultiMap.create();
    public final List<AClientRecipeModRestriction> MOD_CACHE = new ArrayList<>();

    public void reloadBeforeScripts() {
        restrictions.clear();
        CACHE.clear();
        MOD_CACHE.clear();
    }

    public void addRestriction(String stage, @NotNull AClientRecipeRestriction restriction) {
        if (restriction.type() == null) {
            AStages.LOGGER.error("Recipe type for restriction {} is null!", restriction.id());
        }

        var newList = restrictions.getOrDefault(stage, new ArrayList<>());
        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id(), restriction.id())); }
        newList.add(restriction);
        restrictions.put(stage, newList);

        CACHE.put(restriction.type(), restriction);
    }

    public void addRestriction(AClientRecipeModRestriction restriction) {
        MOD_CACHE.add(restriction);
    }

    public Map<String, Set<ResourceLocation>> getAllRecipesForType(RecipeType<?> type) {
        var toReturn = new HashMap<String, Set<ResourceLocation>>();

        for (var restriction : CACHE.get(type)) {
            toReturn.computeIfAbsent(restriction.stage(), m -> new HashSet<>()).addAll(restriction.recipes());
        }

        return toReturn;
    }
}
