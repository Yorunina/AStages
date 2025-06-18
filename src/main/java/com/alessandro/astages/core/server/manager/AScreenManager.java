package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.AScreenRestriction;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AScreenManager extends AManager<AScreenRestriction, MenuType<?>, AbstractContainerMenu> {
    public AScreenRestriction getRestriction(Player player, AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity) {
        return getRestrictions().stream().filter(r -> !AStagesUtil.hasStage(player, r.getStage()) && r.isRestricted(menu, state, entity)).findFirst().orElse(null);
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.SCREEN;
    }

//    public final OrderedMultiMap<MenuType<?>, AScreenRestriction> CACHE = OrderedMultiMap.create();

//    @Override
//    public void reloadBeforeScripts() {
//        super.reloadBeforeScripts();
//        CACHE.clear();
//    }

//    @Override
//    public void addRestriction(AScreenRestriction restriction) {
//        super.addRestriction(restriction);
//
//        for (var menu : restriction.getMenus()) {
//            CACHE.put(menu, restriction);
//        }
//    }

//    @Override
//    public AScreenRestriction getRestriction(Player player, MenuType<?> type) {
//        return getRestrictionFromCache(CACHE, type, player);
//    }

//    public AScreenRestriction getRestriction(Player player, MenuType<?> type, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable AbstractContainerMenu container) {
//        var restrictions = CACHE.get(type);
//
//        if (!restrictions.isEmpty()) {
//            for (var restriction : restrictions) {
//                if (!AStagesUtil.hasStage(player, restriction.getStage()) && restriction.isRestricted(type, state, entity, container)) {
//                    return restriction;
//                }
//            }
//        }
//
//        return null;
//    }

//    @Override
//    public void removeRestriction(String id) {
//        super.removeRestriction(id);
//        CACHE.removeValues(restriction -> restriction.getId().equals(id));
//    }
}
