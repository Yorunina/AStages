package com.alessandro.astages.core.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.recipe.ABaseRecipeRestriction;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.recipe.RecipeModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.recipe.RecipeSyncerS2CPacket;
import com.alessandro.astages.store.ClientSynchronizable;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public class ARecipeManager implements ClientSynchronizable {
    private final List<ABaseRecipeRestriction<?, ?, ?>> restrictions = new ArrayList<>();
    private final Map<String, ABaseRecipeRestriction<?, ?, ?>> IDS = new HashMap<>();

    private final List<ARecipeRestriction> recipes = new ArrayList<>();
    private final OrderedMultiMap<ResourceLocation, ARecipeRestriction> RECIPE_CACHE = OrderedMultiMap.create();
    private final List<ARecipeModRestriction> mods = new ArrayList<>();

    public List<ABaseRecipeRestriction<?, ?, ?>> getRestrictions() {
        return restrictions;
    }

    public List<ARecipeRestriction> getRecipeRestrictions() {
        return recipes;
    }

    public List<ARecipeModRestriction> getModRestrictions() {
        return mods;
    }

    public void reloadBeforeScripts() {
        restrictions.clear();
        IDS.clear();

        recipes.clear();
        RECIPE_CACHE.clear();
        mods.clear();
    }

    public ABaseRecipeRestriction<?, ?, ?> getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public ABaseRecipeRestriction<?, ?, ?> getRestriction(Player player, RecipeWrapper wrapper) {
        var modRestriction = mods.stream().filter(r -> r.isRestricted(wrapper) && !AStagesUtil.hasStage(player, r.getStage())).findFirst().orElse(null);
        if (modRestriction != null) { return modRestriction; }

        return getRestrictionFromCache(RECIPE_CACHE, wrapper.recipe(), player);
    }

    public ARecipeRestriction getRestrictionFromCache(OrderedMultiMap<ResourceLocation, ARecipeRestriction> cache, ResourceLocation value, Player player) {
        var restrictions = cache.get(value);

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (!AStagesUtil.hasStage(player, restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public void addRestriction(ARecipeRestriction restriction) {
        if (commonAddOperations(restriction)) {
            recipes.add(restriction);

            for (var recipe : restriction.getRecipes()) {
                RECIPE_CACHE.put(recipe, restriction);
            }
        }
    }

    public void addRestriction(ARecipeModRestriction restriction) {
        if (commonAddOperations(restriction)) {
            mods.add(restriction);
        }
    }

    private boolean commonAddOperations(@NotNull ABaseRecipeRestriction<?, ?, ?> restriction) {
        if (IDS.containsKey(restriction.getId())) {
            if (AStagesCommon.ENABLE_LOGS.get()) {
                AStages.LOGGER.warn("Restriction with id {} already found!", restriction.getId());
            }

            return false;
        }

        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);

        ARestrictionManager.ALL_STAGES.add(restriction.getStage());
        return true;
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        recipes.forEach(restriction -> ModNetworking.sendTo(player, new RecipeSyncerS2CPacket(restriction)));
        mods.forEach(restriction -> ModNetworking.sendTo(player, new RecipeModSyncerS2CPacket(restriction)));
    }
}