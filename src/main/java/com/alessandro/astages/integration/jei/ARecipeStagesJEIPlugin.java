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
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.util.thread.EffectiveSide;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@JeiPlugin
public class ARecipeStagesJEIPlugin implements IModPlugin {
    private IJeiRuntime runtime;
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(AStages.MODID, "recipe_jei");

    public ARecipeStagesJEIPlugin() {
        if (!Mods.JEI.isLoaded()) return;

        if (EffectiveSide.get().isClient()) {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientRecipeUpdateEvent.class, e -> updateRecipeGui());

            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeStagesEvent.class, e -> {
                if (e.getOperation() != PlayerStage.Operation.LOGIN && e.getOperation() != PlayerStage.Operation.GET) {
                    AClientRestrictionManager.setWaitingForRecipeUpdate(true);
                    updateRecipeGui();
                }
            });
        }
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
//        var recipe = runtime.getRecipeManager().createRecipeLookup(RecipeTypes.CRAFTING).includeHidden().get().filter(r -> r.getId().equals(new ResourceLocation("minecraft", "birch_wood"))).findFirst().get();
//        runtime.getRecipeManager().hideRecipes(RecipeTypes.CRAFTING, List.of(recipe));
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

    private <C extends Container, T extends Recipe<C>> void updateRecipesForType(RecipeType<T> vanillaType, mezz.jei.api.recipe.RecipeType<T> jeiType) {
        if (runtime == null) { return; }

        var map = AClientRestrictionManager.RECIPE_INSTANCE.getAllRecipesForType(vanillaType);
        List<T> recipeList;
        Supplier<Stream<T>> lookup = () -> runtime.getRecipeManager().createRecipeLookup(jeiType).includeHidden().get();

        for (var stage : map.keySet()) {
            recipeList = lookup.get().filter(c -> map.get(stage).contains(c.getId())).toList();

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

    private <C extends Container, T extends Recipe<C>> void restrictAllRecipesForModAndType(mezz.jei.api.recipe.RecipeType<T> type, String modId, String stage) {
        var newList = runtime.getRecipeManager().createRecipeLookup(type).includeHidden().get()
            .filter(r -> r.getId().getNamespace().equals(modId))
            .toList();

        if (ClientPlayerStage.hasStage(stage)) {
            runtime.getRecipeManager().unhideRecipes(type, newList);
        } else {
            runtime.getRecipeManager().hideRecipes(type, newList);
        }
    }
}
