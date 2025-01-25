package com.alessandro.astages.integration.jei;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.alessandro.astages.integration.Mods;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class ARecipeStagesJEIPlugin implements IModPlugin {
    private IJeiRuntime runtime;
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(AStages.MODID, "recipe_jei");

    public ARecipeStagesJEIPlugin() {
        if (!Mods.JEI.isLoaded()) return;

        if (EffectiveSide.get().isClient()) {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientRecipeUpdateEvent.class, e -> updateRecipeGui());
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeStagesEvent.class, e -> updateRecipeGui());
            MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, false, RecipesUpdatedEvent.class, e -> updateRecipeGui());
        }
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    public void updateRecipeGui() {
        updateRecipesForType(RecipeType.CRAFTING, RecipeTypes.CRAFTING);
        updateRecipesForType(RecipeType.SMELTING, RecipeTypes.SMELTING);
        updateRecipesForType(RecipeType.SMOKING, RecipeTypes.SMOKING);
        updateRecipesForType(RecipeType.CAMPFIRE_COOKING, RecipeTypes.CAMPFIRE_COOKING);
        updateRecipesForType(RecipeType.BLASTING, RecipeTypes.BLASTING);
        updateRecipesForType(RecipeType.SMITHING, RecipeTypes.SMITHING);
    }

    @SuppressWarnings("all")
    private <C extends Container, T extends Recipe<C>> void updateRecipesForType(RecipeType<T> vanillaType, mezz.jei.api.recipe.RecipeType<T> jeiType) {
        if (runtime == null) { return; }

        var map = AClientRestrictionManager.RECIPE_INSTANCE.getAllRecipesForType(vanillaType);
        List<T> recipeList;
        var lookup = runtime.getRecipeManager().createRecipeLookup(jeiType).includeHidden().get();

        for (var stage : map.keySet()) {
            recipeList = lookup.filter(c -> map.get(stage).contains(c.getId())).toList();

            if (ClientPlayerStage.hasStage(stage)) {
                runtime.getRecipeManager().unhideRecipes(jeiType, recipeList);
            } else {
                runtime.getRecipeManager().hideRecipes(jeiType, recipeList);
            }
        }
    }
}
