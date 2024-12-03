package com.alessandro.astages.core;

import com.alessandro.astages.AStages;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.ud.JeiRecipeSyncerS2CPacket;
import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ARecipeManager implements AManager<ARecipeRestriction, ARecipeManager.RecipeWrapper> {
    private final Map<String, List<ARecipeRestriction>> restrictions = new HashMap<>();

    public Map<String, List<ARecipeRestriction>> getRestrictions() {
        return restrictions;
    }

    public void synchronizeWithClient(ServerPlayer player) {
        restrictions.forEach((s, restrictions) -> restrictions.forEach(r -> ModNetworking.sendToPlayer(new JeiRecipeSyncerS2CPacket(r.id, s, r.type, r.recipes), player)));
    }

    public boolean isRestrictionListEmpty() {
        return restrictions.isEmpty();
    }

    @Override
    public void reloadBeforeScripts() {
        restrictions.clear();
    }

    @Override
    public void addRestriction(String stage, @NotNull ARecipeRestriction restriction) {
        if (restriction.type == null) {
            AStages.LOGGER.error("Recipe type for restriction {} is null!", restriction.id);
        }

        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public ARecipeRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (ARecipeRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public ARecipeRestriction getRestriction(Player player, RecipeWrapper recipe) {
        for (String stage : restrictions.keySet()) {
            for (ARecipeRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(recipe.type, recipe.recipe) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public record RecipeWrapper(RecipeType<?> type, ResourceLocation recipe) { }
}
