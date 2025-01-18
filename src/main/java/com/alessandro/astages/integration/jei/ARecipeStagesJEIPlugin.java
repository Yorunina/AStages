package com.alessandro.astages.integration.jei;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.core.client.AClientRecipeRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.alessandro.astages.integration.Mods;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

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

    @SuppressWarnings("unchecked")
    public <C extends Container, T extends Recipe<C>> void updateRecipeGui() {
        if (runtime == null) { return; }
//        var recipeManager = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getRecipeManager() : null;
//        if (recipeManager == null) { return; }
//
//        AClientRestrictionManager.RECIPE_INSTANCE.generateCache();
//        // CRAFTING SHAPED AND SHAPELESS
//        runtime.getRecipeManager().hideRecipes(RecipeTypes.CRAFTING, AClientRestrictionManager.RECIPE_INSTANCE.getAllRecipesForType(RecipeType.CRAFTING));
//
//
        var categories = runtime.getRecipeManager().createRecipeCategoryLookup().get().toList();

        for (Map.Entry<String, List<AClientRecipeRestriction>> entry : AClientRestrictionManager.RECIPE_INSTANCE.restrictions.entrySet()) {
            for (var restriction : entry.getValue()) {
                recipeLoop:
                for (var recipeLocation : restriction.recipes()) {
                    for (var category : categories) {

                        var c = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager().getAllRecipesFor((net.minecraft.world.item.crafting.RecipeType<T>) restriction.type());
                        if (!c.isEmpty()) {
                            var caster = c.get(0);
                            if (!canCast(category.getRecipeType().getRecipeClass(), caster.getClass())) {
                                continue;
                            }

                            var recipes = (Stream<Recipe<?>>) runtime.getRecipeManager().createRecipeLookup(category.getRecipeType()).includeHidden().get();
                            var recipe = (Recipe<?>) recipes.filter(r -> r.getId().equals(recipeLocation)).findFirst().orElse(null);

                            if (recipe == null) {
                                continue;
                            }

                            if (!ClientPlayerStage.hasStage(entry.getKey())) {
                                runtime.getRecipeManager().hideRecipes(category.getRecipeType(), cast(Collections.singletonList(recipe)));
                                break recipeLoop;
                            } else {
                                runtime.getRecipeManager().unhideRecipes(category.getRecipeType(), cast(Collections.singletonList(recipe)));
                                break recipeLoop;
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean canCast(Class<?> obj, Class<?> caster){
        try {
            if(obj == caster) return true;

            List<Class<?>> d1 = getParent(obj);
            if(d1.contains(caster)) return true;

            d1 = getParent(caster);
            if(d1.contains(obj)) return true;
        } catch (Exception e) {
            AStages.LOGGER.debug(e.getLocalizedMessage());
        }

        return false;
    }

    public static @NotNull List<Class<?>> getParent(@NotNull Class<?> obj){
        List<Class<?>> classes = Collections.synchronizedList(new ArrayList<>());

        for (Class<?> anInterface : obj.getInterfaces()) {
            classes.add(anInterface);
            classes.addAll(getParent(anInterface));
        }

        if (obj.getSuperclass() != null) {
            classes.add(obj.getSuperclass());
            classes.addAll(getParent(obj.getSuperclass()));
        }

        return classes;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }
}
