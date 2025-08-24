package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.recipe.ABaseRecipeRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.recipe.RecipeModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.recipe.RecipeSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestRestrictionDeleteS2CPacket;
import com.alessandro.astages.store.ClientSynchronizable;
import com.alessandro.astages.store.server.AMinimalManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NotNullParams
public class ARecipeManager implements AMinimalManager<ABaseRecipeRestriction<?, ?, ?>>, ClientSynchronizable {
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

    @Override
    public void reloadBeforeScripts() {
        restrictions.clear();
        IDS.clear();

        recipes.clear();
        RECIPE_CACHE.clear();
        mods.clear();
    }

    @Override
    public void reloadAfterScripts() { }

    @Override
    public ABaseRecipeRestriction<?, ?, ?> getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public ABaseRecipeRestriction<?, ?, ?> getRestriction(Player player, RecipeWrapper wrapper) {
        var modRestriction = mods.stream().filter(r -> r.isRestricted(wrapper) && !AStagesUtil.hasStage(player, r.getStage())).findFirst().orElse(null);
        if (modRestriction != null) { return modRestriction; }

        return getRestrictionFromCache(RECIPE_CACHE, wrapper.recipe(), player);
    }

    public ABaseRecipeRestriction<?, ?, ?> getRestriction(MinecraftServer server, RecipeWrapper wrapper) {
        var data = ServerStageData.getData(server);
        var modRestriction = mods.stream().filter(r -> r.isRestricted(wrapper) && !data.has(r.getStage())).findFirst().orElse(null);
        if (modRestriction != null) { return modRestriction; }

        return getRestrictionFromCache(RECIPE_CACHE, wrapper.recipe(), server);
    }

    public ABaseRecipeRestriction<?, ?, ?> getRestriction(RecipeWrapper wrapper, @Nullable Player player, @Nullable MinecraftServer server) {
        ABaseRecipeRestriction<?, ?, ?> serverRestriction = null;
        ABaseRecipeRestriction<?, ?, ?> playerRestriction = null;

        if (server != null) { serverRestriction = getRestriction(server, wrapper); }
        if (serverRestriction == null) { // If the stage is unlocked in the server, pass!
            return null;
        }

        if (player != null) { playerRestriction = getRestriction(player, wrapper); }
        return playerRestriction;
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

    public ARecipeRestriction getRestrictionFromCache(OrderedMultiMap<ResourceLocation, ARecipeRestriction> cache, ResourceLocation value, MinecraftServer server) {
        var restrictions = cache.get(value);
        var data = ServerStageData.getData(server);

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (!data.has(restriction.getStage())) {
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

    private boolean commonAddOperations(ABaseRecipeRestriction<?, ?, ?> restriction) {
        if (IDS.containsKey(restriction.getId())) {
            if (AStagesCommon.ENABLE_LOGS.get()) {
                AStages.LOGGER.warn("Restriction with id {} already found!", restriction.getId());
            }

            return false;
        }

        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);

        ARestrictionManager.ALL_IDS.add(restriction.getId());
        ARestrictionManager.ALL_STAGES.add(restriction.getStage());
        return true;
    }

    @Override
    public void removeRestriction(String id) {
        restrictions.removeIf(restriction -> restriction.getId().equals(id));
        recipes.removeIf(restriction -> restriction.getId().equals(id));
        mods.removeIf(restriction -> restriction.getId().equals(id));
        RECIPE_CACHE.removeValues(restriction -> restriction.getId().equals(id));
        IDS.remove(id);

        ANetworking.sendTo(null, new RequestRestrictionDeleteS2CPacket(id, associatedType()));
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        recipes.forEach(restriction -> ANetworking.sendTo(player, new RecipeSyncerS2CPacket(restriction)));
        mods.forEach(restriction -> ANetworking.sendTo(player, new RecipeModSyncerS2CPacket(restriction)));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.RECIPE;
    }
}
