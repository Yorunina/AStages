package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.AMobRestriction;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.mob.MobSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestRestrictionDeleteS2CPacket;
import com.alessandro.astages.store.ClientSynchronizable;
import com.alessandro.astages.store.ServerStageReadable;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

@NotNullParams
public class AMobManager extends AManager<AMobRestriction, EntityType<?>, EntityType<?>> implements ServerStageReadable<AMobRestriction, EntityType<?>>, ClientSynchronizable {
    private final OrderedMultiMap<EntityType<?>, AMobRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(AMobRestriction restriction) {
        super.addRestriction(restriction);

        for (var type : restriction.getMobs()) {
            CACHE.put(type, restriction);
        }
    }

    @Override
    public AMobRestriction getRestriction(Player player, EntityType<?> type) {
        return getRestrictionFromCache(CACHE, type, player);
    }

    @Override
    public AMobRestriction getRestriction(MinecraftServer server, EntityType<?> type) {
        return getRestrictionFromCache(CACHE, type, server);
    }

//    @Override
//    public AMobRestriction getRestriction(EntityType<?> type, @Nullable Player player, @Nullable MinecraftServer server) {
//        AMobRestriction serverRestriction = null;
//        AMobRestriction playerRestriction = null;
//
//        if (server != null) { serverRestriction = getRestriction(server, type); }
//        if (player != null) { playerRestriction = getRestriction(player, type); }
//
//        if (serverRestriction == null) { // If the stage is unlocked in the server, pass!
//            return null;
//        }
//
//        return playerRestriction;
//    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));

        ANetworking.sendTo(null, new RequestRestrictionDeleteS2CPacket(id, associatedType()));
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        getRestrictions().forEach(restriction -> ANetworking.sendTo(player, new MobSyncerS2CPacket(restriction)));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.MOB;
    }
}
