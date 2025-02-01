package com.alessandro.astages.core.manager;

import com.alessandro.astages.core.restriction.ARecipeRestriction;
import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.syncer.JeiRecipeModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.syncer.JeiRecipeSyncerS2CPacket;
import com.alessandro.astages.networking.packet.syncer.RequestJeiRecipeReloadS2CPacket;
import com.alessandro.astages.store.AManager;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class ARecipeManager extends AManager<ARecipeRestriction, RecipeWrapper, RecipeWrapper> {
    public final OrderedMultiMap<RecipeType<?>, ARecipeRestriction> CACHE = OrderedMultiMap.create();
    public final List<ARecipeRestriction> MOD_CACHE = new ArrayList<>();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
        MOD_CACHE.clear();
    }

    @Override
    public void addRestriction(ARecipeRestriction restriction) {
        super.addRestriction(restriction);
        if (restriction.getType() != null) {
            CACHE.put(restriction.getType(), restriction);
        } else if (restriction.getModId() != null) {
            MOD_CACHE.add(restriction);
        }
    }

    @Override
    public ARecipeRestriction getRestriction(Player player, RecipeWrapper wrapper) {
        var modRestriction = getRestriction(player, new RecipeModWrapper(wrapper.recipe().getNamespace()));
        if (modRestriction != null) { return modRestriction; }

        var restrictions = CACHE.get(wrapper.type());

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (restriction.isRestricted(wrapper) && !AStagesUtil.hasStage(player, restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public ARecipeRestriction getRestriction(Player player, RecipeModWrapper wrapper) {
        for (var restriction : MOD_CACHE) {
            if (restriction.isRestricted(wrapper) && !AStagesUtil.hasStage(player, restriction.getStage())) {
                return restriction;
            }
        }

        return null;
    }

    public void synchronizeWithClient(ServerPlayer player) {
        for (var type : CACHE.keySet()) {
            for (var restriction : CACHE.get(type)) {
                ModNetworking.sendToPlayer(new JeiRecipeSyncerS2CPacket(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.getType(), restriction.getRecipes()), player);
            }
        }

        for (var restriction : MOD_CACHE) {
            ModNetworking.sendToPlayer(new JeiRecipeModSyncerS2CPacket(restriction.getId(), restriction.getStage(), restriction.getModId()), player);
        }

        ModNetworking.sendToPlayer(new RequestJeiRecipeReloadS2CPacket(), player);

        // restrictions.forEach((s, restrictions) -> restrictions.forEach(r -> ModNetworking.sendToPlayer(new JeiRecipeSyncerS2CPacket(r.getId(), s, r.getType(), r.getRecipes()), player)));
    }
}
