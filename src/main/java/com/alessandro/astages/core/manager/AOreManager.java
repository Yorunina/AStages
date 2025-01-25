package com.alessandro.astages.core.manager;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.AOreRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.syncer.OreSyncerS2CPacket;
import com.alessandro.astages.store.AManager;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AOreManager extends AManager<AOreRestriction, OreWrapper, BlockState> {
    public final OrderedMultiMap<BlockState, AOreRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(AOreRestriction restriction) {
        super.addRestriction(restriction);

        ARestrictionManager.ORE_STAGES.add(restriction.getStage());
        CACHE.put(restriction.getOriginal(), restriction);
    }

    @Override
    public AOreRestriction getRestriction(Player player, BlockState state) {
        return getRestrictionFromCache(CACHE, state, player);
    }

    public void synchronizeWithClient(ServerPlayer player) {
        restrictions.forEach((s, restrictions) -> {
            restrictions.forEach(r -> ModNetworking.sendToPlayer(new OreSyncerS2CPacket(r.getId(), s, r.getOriginal(), r.getReplacement(), true), player));
        });
    }
}
