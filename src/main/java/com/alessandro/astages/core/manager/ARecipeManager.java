package com.alessandro.astages.core.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.restriction.ARecipeRestriction;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.syncer.JeiRecipeSyncerS2CPacket;
import com.alessandro.astages.store.AManager;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ARecipeManager extends AManager<ARecipeRestriction, RecipeWrapper, RecipeWrapper> {
    public final OrderedMultiMap<RecipeType<?>, ARecipeRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(ARecipeRestriction restriction) {
        super.addRestriction(restriction);
        CACHE.put(restriction.getType(), restriction);
    }

    @Override
    public ARecipeRestriction getRestriction(Player player, RecipeWrapper wrapper) {
        var restrictions = CACHE.get(wrapper.type());

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (restriction.getRecipes().contains(wrapper.recipe())) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public void synchronizeWithClient(ServerPlayer player) {
        AStages.LOGGER.debug("CACHE: {}", CACHE);

        for (var type : CACHE.keySet()) {
            for (var restriction : CACHE.get(type)) {
                ModNetworking.sendToPlayer(new JeiRecipeSyncerS2CPacket(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.getType(), restriction.getRecipes()), player);
            }
        }

        // restrictions.forEach((s, restrictions) -> restrictions.forEach(r -> ModNetworking.sendToPlayer(new JeiRecipeSyncerS2CPacket(r.getId(), s, r.getType(), r.getRecipes()), player)));
    }
}
