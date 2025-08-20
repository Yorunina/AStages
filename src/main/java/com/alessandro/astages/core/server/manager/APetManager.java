package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.APetRestriction;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.OrderedMultiMap;
import com.alessandro.astages.util.annotations.NotNullParams;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

@NotNullParams
public class APetManager extends AManager<APetRestriction, EntityType<?>, EntityType<?>> {
    public final OrderedMultiMap<EntityType<?>, APetRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(APetRestriction restriction) {
        super.addRestriction(restriction);

        for (var type : restriction.getPets()) {
            CACHE.put(type, restriction);
        }
    }

    @Override
    public APetRestriction getRestriction(Player player, EntityType<?> type) {
        return getRestrictionFromCache(CACHE, type, player);
    }

    @Override
    public APetRestriction getRestriction(MinecraftServer server, EntityType<?> type) {
        return getRestrictionFromCache(CACHE, type, server);
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.PET;
    }
}
