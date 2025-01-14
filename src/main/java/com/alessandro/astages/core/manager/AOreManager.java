package com.alessandro.astages.core.manager;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.AOreRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.packet.syncer.OreSyncerS2CPacket;
import com.alessandro.astages.store.AManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AOreManager extends AManager<AOreRestriction, OreWrapper, BlockState> {
    @Override
    public void addRestriction(AOreRestriction restriction) {
        super.addRestriction(restriction);

        ARestrictionManager.ORE_STAGES.add(restriction.getStage());
    }

    public void synchronizeWithClient(ServerPlayer player) {
        restrictions.forEach((s, restrictions) -> {
            restrictions.forEach(r -> PacketDistributor.sendToPlayer(player, new OreSyncerS2CPacket(r.getId(), s, r.getOriginal(), r.getReplacement(), true)));
        });
    }
}
