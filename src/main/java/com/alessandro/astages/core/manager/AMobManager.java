package com.alessandro.astages.core.manager;

import com.alessandro.astages.core.restriction.AMobRestriction;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.syncer.MobSyncerS2CPacket;
import com.alessandro.astages.store.AManager;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AMobManager extends AManager<AMobRestriction, EntityType<?>, EntityType<?>> {
    public final OrderedMultiMap<EntityType<?>, AMobRestriction> CACHE = OrderedMultiMap.create();

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

    public void synchronizeWithClient(ServerPlayer player) {
        getRestrictions().forEach(r -> ModNetworking.sendToPlayer(new MobSyncerS2CPacket(r.getId(), r.getStage(), r.getMobs(), r.get(Attributes.Mob.JADE_MOB_MESSAGE).get()), player));
    }
}
