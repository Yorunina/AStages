package com.alessandro.astages.core.manager;

import com.alessandro.astages.core.restriction.AMobRestriction;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.mob.MobSyncerS2CPacket;
import com.alessandro.astages.store.AManager;
import com.alessandro.astages.store.ClientSynchronizable;
import com.alessandro.astages.store.ServerStageReadable;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AMobManager extends AManager<AMobRestriction, EntityType<?>, EntityType<?>> implements ServerStageReadable<AMobRestriction, EntityType<?>>, ClientSynchronizable {
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

    @Override
    public AMobRestriction getRestriction(MinecraftServer server, EntityType<?> type) {
        return getRestrictionFromCache(CACHE, type, server);
    }

    @Override
    public AMobRestriction getRestriction(EntityType<?> type, @Nullable Player player, @Nullable MinecraftServer server) {
        AMobRestriction serverRestriction = null;
        AMobRestriction playerRestriction = null;

        if (server != null) { serverRestriction = getRestriction(server, type); }
        if (player != null) { playerRestriction = getRestriction(player, type); }

        if (serverRestriction == null) { // If the stage is unlocked in the server, pass!
            return null;
        }

        return playerRestriction;
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        getRestrictions().forEach(restriction -> ModNetworking.sendTo(player, new MobSyncerS2CPacket(restriction)));
    }
}
