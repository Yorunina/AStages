package com.alessandro.astages.core.manager;

import com.alessandro.astages.core.restriction.APetRestriction;
import com.alessandro.astages.store.AManager;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
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
}
