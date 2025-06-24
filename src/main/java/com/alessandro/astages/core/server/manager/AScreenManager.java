package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.AScreenRestriction;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AScreenManager extends AManager<AScreenRestriction, MenuType<?>, MenuType<?>> {
    public final OrderedMultiMap<MenuType<?>, AScreenRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(AScreenRestriction restriction) {
        super.addRestriction(restriction);

        for (var menu : restriction.getMenus()) {
            CACHE.put(menu, restriction);
        }
    }

    @Override
    public AScreenRestriction getRestriction(Player player, MenuType<?> type) {
        return getRestrictionFromCache(CACHE, type, player);
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.SCREEN;
    }
}
