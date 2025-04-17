package com.alessandro.astages.integration.jei;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.alessandro.astages.integration.Mods;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@JeiPlugin
public class ARecipeStagesJEIPlugin implements IModPlugin {
    private IJeiRuntime runtime;
    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(AStages.MODID, "recipe_jei");

    public ARecipeStagesJEIPlugin() {
        if (!Mods.JEI.isLoaded()) return;

        if (EffectiveSide.get().isClient()) {
            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientRecipeUpdateEvent.class, e -> updateRecipeGui());

            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeStagesEvent.class, e -> {
                if (e.getOperation() != PlayerStage.Operation.LOGIN && e.getOperation() != PlayerStage.Operation.GET) {
                    AClientRestrictionManager.setWaitingForRecipeUpdate(true);
                    updateRecipeGui();
                }
            });
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
        if (runtime != null && AClientRestrictionManager.waitingForRecipeUpdate && !AClientRestrictionManager.jeiIsReloading) {
            AStages.LOGGER.info("AStages client recipe update started!");
            var time = System.currentTimeMillis();

            restrictAllRecipesForMods();

            updateRecipesForType(RecipeType.CRAFTING, RecipeTypes.CRAFTING);
            updateRecipesForType(RecipeType.SMELTING, RecipeTypes.SMELTING);
            updateRecipesForType(RecipeType.SMOKING, RecipeTypes.SMOKING);
            updateRecipesForType(RecipeType.CAMPFIRE_COOKING, RecipeTypes.CAMPFIRE_COOKING);
            updateRecipesForType(RecipeType.BLASTING, RecipeTypes.BLASTING);
            updateRecipesForType(RecipeType.SMITHING, RecipeTypes.SMITHING);
            updateRecipesForType(RecipeType.STONECUTTING, RecipeTypes.STONECUTTING);

            AStages.LOGGER.info("AStages recipe update completed in {} ms!", System.currentTimeMillis() - time);
            AClientRestrictionManager.setWaitingForRecipeUpdate(false);
        }
    }

    private <I extends RecipeInput, T extends Recipe<I>> void updateRecipesForType(RecipeType<T> vanillaType, mezz.jei.api.recipe.RecipeType<RecipeHolder<T>> jeiType) {
        if (runtime == null) { return; }

        var map = AClientRestrictionManager.RECIPE_INSTANCE.getAllRecipesForType(vanillaType);
        List<RecipeHolder<T>> recipeList;
        Supplier<Stream<RecipeHolder<T>>> lookup = () -> runtime.getRecipeManager().createRecipeLookup(jeiType).includeHidden().get();

        for (var stage : map.keySet()) {
            recipeList = lookup.get().filter(c -> map.get(stage).contains(c.id())).toList();

            if (ClientPlayerStage.hasStage(stage)) {
                runtime.getRecipeManager().unhideRecipes(jeiType, recipeList);
            } else {
                runtime.getRecipeManager().hideRecipes(jeiType, recipeList);
            }
        }
    }

    private void restrictAllRecipesForMods() {
        for (var mod : AClientRestrictionManager.RECIPE_INSTANCE.MOD_CACHE) {
            restrictAllRecipesForModAndType(RecipeTypes.CRAFTING, mod.modId(), mod.stage());
            restrictAllRecipesForModAndType(RecipeTypes.SMELTING, mod.modId(), mod.stage());
            restrictAllRecipesForModAndType(RecipeTypes.SMOKING, mod.modId(), mod.stage());
            restrictAllRecipesForModAndType(RecipeTypes.CAMPFIRE_COOKING, mod.modId(), mod.stage());
            restrictAllRecipesForModAndType(RecipeTypes.BLASTING, mod.modId(), mod.stage());
            restrictAllRecipesForModAndType(RecipeTypes.SMITHING, mod.modId(), mod.stage());
            restrictAllRecipesForModAndType(RecipeTypes.STONECUTTING, mod.modId(), mod.stage());
        }
    }

    private <I extends RecipeInput, T extends Recipe<I>> void restrictAllRecipesForModAndType(mezz.jei.api.recipe.RecipeType<RecipeHolder<T>> type, String modId, String stage) {
        var newList = runtime.getRecipeManager().createRecipeLookup(type).includeHidden().get()
            .filter(r -> r.id().getNamespace().equals(modId))
            .toList();

        if (ClientPlayerStage.hasStage(stage)) {
            runtime.getRecipeManager().unhideRecipes(type, newList);
        } else {
            runtime.getRecipeManager().hideRecipes(type, newList);
        }
    }
}
