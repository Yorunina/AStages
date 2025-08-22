package com.alessandro.astages.store.server;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.ServerStageReadable;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.api.util.OrderedMultiMap;
import com.alessandro.astages.api.annotation.nullability.NotNullParams;
import com.alessandro.astages.api.annotation.nullability.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for almost all Managers related to AStages!
 *
 * @param <R> The restriction associated to this manager
 * @param <U> For restrict method object type
 * @param <V> For isRestricted method object type
 */
@NotNullParams
public abstract class AManager<R extends ARestriction<R, U, V>, U, V> implements AMinimalManager<R>, ServerStageReadable<R, V> {
    private final List<R> restrictions = new ArrayList<>();
    private final Map<String, R> IDS = new HashMap<>();

    public List<R> getRestrictions() {
        return restrictions;
    }

    @Override
    public void reloadBeforeScripts() {
        restrictions.clear();
        IDS.clear();
    }

    @Override
    public void reloadAfterScripts() { }

    public R getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public R getRestriction(Player player, V object) {
        return restrictions.stream().filter(r -> !AStagesUtil.hasStage(player, r.getStage()) && r.isRestricted(object)).findFirst().orElse(null);
    }

    public List<String> getIds() {
        return IDS.keySet().stream().toList();
    }

    public void addRestriction(R restriction) {
        if (IDS.containsKey(restriction.getId())) {
            if (AStagesCommon.ENABLE_LOGS.get()) {
                AStages.LOGGER.warn("Restriction with id {} already found!", restriction.getId());
            }

            return;
        }

        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);
        ARestrictionManager.ALL_IDS.add(restriction.getId());
        if (considerGlobalStages()) { ARestrictionManager.ALL_STAGES.add(restriction.getStage()); }
    }

    @Override
    public R getRestriction(MinecraftServer server, V object) {
        var data = ServerStageData.getData(server);
        return restrictions.stream().filter(r -> !data.has(r.getStage()) && r.isRestricted(object)).findFirst().orElse(null);
    }

    @Override
    public R getRestriction(V object, @Nullable Player player, @Nullable MinecraftServer server) {
        R serverRestriction = null;
        R playerRestriction = null;

        if (server != null) { serverRestriction = getRestriction(server, object); }
        if (serverRestriction == null) { // If the stage is unlocked in the server, pass!
            return null;
        }

        if (player != null) { playerRestriction = getRestriction(player, object); }
        return playerRestriction;
    }

    public <W> R getRestrictionFromCache(OrderedMultiMap<W, R> cache, W value, Player player) {
        var restrictions = cache.get(value);

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (!AStagesUtil.hasStage(player, restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public <W> R getRestrictionFromCache(OrderedMultiMap<W, R> cache, W value, MinecraftServer server) {
        var restrictions = cache.get(value);
        var data = ServerStageData.getData(server);

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (!data.has(restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public void removeRestriction(String id) {
        IDS.remove(id);
        restrictions.removeIf(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return null;
    }

    public boolean considerGlobalStages() {
        return true;
    }
}
