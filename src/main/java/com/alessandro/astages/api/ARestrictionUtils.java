package com.alessandro.astages.api;

import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.store.server.ARestriction;

@NotNullParamsAndMethodsReturn
public class ARestrictionUtils {
//    public static <R extends ARestriction<R, ?, V>, V> @Nullable ARestriction<R, ?, V> getRestriction(AHolder holder, AStageType type, List<R> restrictions, V object) {
//        return restrictions.stream().filter(r ->
//            AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
//                r.isRestricted(object)
//        ).findFirst().orElse(null);
//    }

    public static  <W, R extends ARestriction<R, ?, ?>> @Nullable R getRestrictionFromCache(AHolder holder, OrderedMultiMap<W, R> cache, W value) {
        if (holder.isServerActive()) {
            var serverRestriction = getRestrictionFromCache(holder, AStageType.SERVER, cache, value);
            if (serverRestriction == null) { return null; }
        }

        if (holder.isPlayerActive()) {
            return getRestrictionFromCache(holder, AStageType.PLAYER, cache, value);
        }

        return null;
    }

    public static  <W, R extends ARestriction<R, ?, ?>> @Nullable R getRestrictionFromCache(AHolder holder, AStageType type, OrderedMultiMap<W, R> cache, W value) {
        var restrictions = cache.get(value);

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (!AStagesUtils.hasStage(holder, type, restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public static void addRestrictionForItem() {

    }
}
