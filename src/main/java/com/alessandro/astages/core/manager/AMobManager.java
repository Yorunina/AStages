package com.alessandro.astages.core.manager;

import com.alessandro.astages.core.restriction.AMobRestriction;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.syncer.MobSyncerS2CPacket;
import com.alessandro.astages.store.AManager;
import com.alessandro.astages.store.Attributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

public class AMobManager extends AManager<AMobRestriction, EntityType<?>, EntityType<?>> {
    public void synchronizeWithClient(ServerPlayer player) {
        restrictions.forEach((s, restrictions) -> {
            restrictions.forEach(r -> ModNetworking.sendToPlayer(new MobSyncerS2CPacket(r.getId(), r.getStage(), r.getMobs(), r.get(Attributes.Mob.JADE_MOB_MESSAGE).get()), player));
        });
    }
}
