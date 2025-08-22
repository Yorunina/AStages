package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.core.server.restriction.AScreenRestriction;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.api.annotation.nullability.NotNullParams;
import com.alessandro.astages.api.annotation.nullability.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class AScreenManager extends AManager<AScreenRestriction, MenuType<?>, AbstractContainerMenu> {
    public AScreenRestriction getRestriction(Player player, AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity) {
        return getRestrictions().stream().filter(r -> !AStagesUtil.hasStage(player, r.getStage()) && r.isRestricted(menu, state, entity)).findFirst().orElse(null);
    }

    public AScreenRestriction getRestriction(MinecraftServer server, AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity) {
        var data = ServerStageData.getData(server);

        return getRestrictions().stream().filter(r -> !data.has(r.getStage()) && r.isRestricted(menu, state, entity)).findFirst().orElse(null);
    }

    public AScreenRestriction getRestriction(AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Player player, @Nullable MinecraftServer server) {
        AScreenRestriction serverRestriction = null;
        AScreenRestriction playerRestriction = null;

        if (server != null) { serverRestriction = getRestriction(server, menu, state, entity); }
        if (serverRestriction == null) { // If the stage is unlocked in the server, pass!
            return null;
        }

        if (player != null) { playerRestriction = getRestriction(player, menu, state, entity); }
        return playerRestriction;
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
