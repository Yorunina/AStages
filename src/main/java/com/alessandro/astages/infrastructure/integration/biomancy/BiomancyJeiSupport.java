package com.alessandro.astages.infrastructure.integration.biomancy;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeRestriction;
import com.github.elenterius.biomancy.crafting.recipe.BioForgingRecipe;
import com.github.elenterius.biomancy.integration.jei.BioForgingCategory;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Isolates Biomancy-specific class references so the JEI plugin class can load safely
 * even when Biomancy is not installed. This class is only referenced when
 * {@code Mods.BIOMANCY.isLoaded()} is true, keeping its class loading lazy.
 *
 * IMPORTANT: We deliberately avoid referencing {@code ModRecipes.BIO_FORGING_RECIPE_TYPE}
 * and {@code SimpleRecipeType.ItemStackRecipeType}. The {@code var} keyword with
 * {@code ModRecipes.BIO_FORGING_RECIPE_TYPE.get()} makes the compiler emit a
 * {@code checkcast SimpleRecipeType$ItemStackRecipeType} instruction, which triggers
 * {@code NoClassDefFoundError} on some client installations where that nested class
 * is not resolvable by the Forge module classloader. We instead look up the
 * {@code RecipeType<?>} by its registry id, returning the erased {@code RecipeType}
 * type with no checkcast to Biomancy's internal implementation class.
 */
public final class BiomancyJeiSupport {
    private static final ResourceLocation BIO_FORGING_ID = new ResourceLocation("biomancy", "bio_forging");

    private BiomancyJeiSupport() {}

    public static void updateBioForgingRecipes(IJeiRuntime runtime) {
        if (runtime == null) { return; }

        // Look up the vanilla RecipeType<?> by registry id. This returns RecipeType
        // (erased) without any checkcast to Biomancy's implementation class.
        RecipeType<?> vanillaType = ForgeRegistries.RECIPE_TYPES.getValue(BIO_FORGING_ID);
        if (vanillaType == null) { return; }

        // Build stage -> restricted-recipe-ids map by iterating the client registry directly.
        // We avoid AClientRestrictionManager#getAllRecipesForType because it relies on
        // RecipeTypeClientCache (HashMap keyed by RecipeType), and Biomancy's
        // AdvancedRecipeType does not override equals/hashCode — if the restriction's
        // type instance and ModRecipes.BIO_FORGING_RECIPE_TYPE.get() are ever distinct
        // instances, the cache lookup returns empty and nothing gets hidden.
        Map<String, Set<ResourceLocation>> map = new HashMap<>();
        for (AClientRecipeRestriction restriction : AClientRestrictionManager.RECIPE_INSTANCE.getRegistry().getRecipeRestrictions()) {
            // Compare by reference; RecipeType instances are singletons registered in the registry,
            // so the restriction's type and the registry lookup return the same instance.
            if (restriction.getType() != vanillaType) { continue; }
            map.computeIfAbsent(restriction.getStage(), s -> new HashSet<>()).addAll(restriction.getRecipes());
        }

        if (map.isEmpty()) { return; }

        // JEI RecipeType<BioForgingRecipe> — this is a JEI interface, safe to reference.
        var jeiType = BioForgingCategory.RECIPE_TYPE;
        Supplier<Stream<BioForgingRecipe>> lookup = () -> runtime.getRecipeManager()
            .createRecipeLookup(jeiType).includeHidden().get();

        var holder = AClientHolder.serverAndPlayer();

        for (var stage : map.keySet()) {
            var restrictedIds = map.get(stage);
            List<BioForgingRecipe> recipeList = lookup.get()
                .filter(c -> restrictedIds.contains(c.getId()))
                .toList();

            if (recipeList.isEmpty()) { continue; }

            // Mirror ABioForgeScreenController's logic: hide only when the player lacks
            // the stage on BOTH server and player scope.
            boolean hasServerStage = AStagesClientUtils.hasStage(holder, AStageType.SERVER, stage);
            boolean hasPlayerStage = AStagesClientUtils.hasStage(holder, AStageType.PLAYER, stage);

            if (!hasServerStage && !hasPlayerStage) {
                runtime.getRecipeManager().hideRecipes(jeiType, recipeList);
            } else {
                runtime.getRecipeManager().unhideRecipes(jeiType, recipeList);
            }
        }
    }
}
