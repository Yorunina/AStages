package com.alessandro.astages.core.manager;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.AOreRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.ore.OreSyncerS2CPacket;
import com.alessandro.astages.store.ReloadType;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.store.AManager;
import com.alessandro.astages.store.ClientSynchronizable;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AOreManager extends AManager<AOreRestriction, OreWrapper, BlockState> implements ClientSynchronizable {
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

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        getRestrictions().forEach(restriction -> ModNetworking.sendTo(player, new OreSyncerS2CPacket(restriction/*, true*/)));
        ModNetworking.sendTo(player, new RequestReloadS2CPacket(ReloadType.ORE));
    }
}
