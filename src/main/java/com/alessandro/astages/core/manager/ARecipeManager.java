package com.alessandro.astages.core.manager;

import com.alessandro.astages.core.restriction.ARecipeRestriction;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.syncer.JeiRecipeSyncerS2CPacket;
import com.alessandro.astages.store.AManager;
import net.minecraft.server.level.ServerPlayer;

public class ARecipeManager extends AManager<ARecipeRestriction, RecipeWrapper, RecipeWrapper> {
    public void synchronizeWithClient(ServerPlayer player) {
        restrictions.forEach((s, restrictions) -> restrictions.forEach(r -> ModNetworking.sendToPlayer(new JeiRecipeSyncerS2CPacket(r.getId(), s, r.getType(), r.getRecipes()), player)));
    }
}
