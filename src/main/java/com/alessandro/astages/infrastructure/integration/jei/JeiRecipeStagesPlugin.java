package com.alessandro.astages.infrastructure.integration.jei;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.engine.AClientRestrictionManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@NotNullParamsAndMethodsReturn
@JeiPlugin
public class JeiRecipeStagesPlugin implements IModPlugin {
    private static IJeiRuntime RUNTIME;

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        RUNTIME = jeiRuntime;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return AResourceLocation.fromNamespaceAndPath("recipe_jei");
    }

    public static void onReloadStarted() { }

    public static void onReloadFinished() {
        updateRecipeGui(AOperation.LOGIN, AStagesClientUtils.getStages(AClientHolder.player()));
    }

    public static void onStagesChanged(AOperation operation, Set<String> syncedStages) {
        updateRecipeGui(operation, syncedStages);
    }

    public static void updateRecipeGui(@Nullable AOperation operation, @Nullable Set<String> syncedStages) {
        if (RUNTIME != null) {
            restrictAllRecipesForMods();

            updateRecipesForType(RecipeType.CRAFTING, RecipeTypes.CRAFTING);
            updateRecipesForType(RecipeType.SMELTING, RecipeTypes.SMELTING);
            updateRecipesForType(RecipeType.SMOKING, RecipeTypes.SMOKING);
            updateRecipesForType(RecipeType.CAMPFIRE_COOKING, RecipeTypes.CAMPFIRE_COOKING);
            updateRecipesForType(RecipeType.BLASTING, RecipeTypes.BLASTING);
            updateRecipesForType(RecipeType.SMITHING, RecipeTypes.SMITHING);
            updateRecipesForType(RecipeType.STONECUTTING, RecipeTypes.STONECUTTING);
        }
    }

    private static <C extends Container, T extends Recipe<C>> void updateRecipesForType(RecipeType<T> vanillaType, mezz.jei.api.recipe.RecipeType<T> jeiType) {
        if (RUNTIME == null) { return; }

        var map = AClientRestrictionManager.RECIPE_INSTANCE.getAllRecipesForType(vanillaType);
        List<T> recipeList;
        Supplier<Stream<T>> lookup = () -> RUNTIME.getRecipeManager().createRecipeLookup(jeiType).includeHidden().get();

        for (var stage : map.keySet()) {
            recipeList = lookup.get().filter(c -> map.get(stage).contains(c.getId())).toList();

            if (AStagesClientUtils.hasStage(AClientHolder.serverAndPlayer(), stage)) {
                RUNTIME.getRecipeManager().unhideRecipes(jeiType, recipeList);
            } else {
                RUNTIME.getRecipeManager().hideRecipes(jeiType, recipeList);
            }
        }
    }

    private static void restrictAllRecipesForMods() {
        for (var mod : AClientRestrictionManager.RECIPE_INSTANCE.getRegistry().getModRestrictions()) {
            var ignored = mod.getIgnoredRecipeIds();
            restrictAllRecipesForModAndType(RecipeTypes.CRAFTING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.SMELTING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.SMOKING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.CAMPFIRE_COOKING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.BLASTING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.SMITHING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.STONECUTTING, mod.getModId(), mod.getStage(), ignored);
        }
    }

    private static <C extends Container, T extends Recipe<C>> void restrictAllRecipesForModAndType(mezz.jei.api.recipe.RecipeType<T> type, String modId, String stage, List<ResourceLocation> ignoredRecipeIds) {
        var newList = RUNTIME.getRecipeManager().createRecipeLookup(type).includeHidden().get()
            .filter(r -> r.getId().getNamespace().equals(modId) && !ignoredRecipeIds.contains(r.getId()))
            .toList();

        if (AStagesClientUtils.hasStage(AClientHolder.serverAndPlayer(), stage)) {
            RUNTIME.getRecipeManager().unhideRecipes(type, newList);
        } else {
            RUNTIME.getRecipeManager().hideRecipes(type, newList);
        }
    }
}