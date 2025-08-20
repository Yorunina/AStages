package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.ADimensionRestriction;
import com.alessandro.astages.store.ServerStageReadable;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.OrderedMultiMap;
import com.alessandro.astages.util.annotations.NotNullParams;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

@NotNullParams
public class ADimensionManager extends AManager<ADimensionRestriction, ResourceLocation, ResourceLocation> implements ServerStageReadable<ADimensionRestriction, ResourceLocation> {
    public final OrderedMultiMap<ResourceLocation, ADimensionRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        CACHE.clear();
    }

    @Override
    public void addRestriction(ADimensionRestriction restriction) {
        super.addRestriction(restriction);

        for (var dimension : restriction.getDimensions()) {
            CACHE.put(dimension, restriction);
        }
    }

    @Override
    public ADimensionRestriction getRestriction(Player player, ResourceLocation dimension) {
        return getRestrictionFromCache(CACHE, dimension, player);
    }

    @Override
    public ADimensionRestriction getRestriction(MinecraftServer server, ResourceLocation dimension) {
        return getRestrictionFromCache(CACHE, dimension, server);
    }

//    @Override
//    public ADimensionRestriction getRestriction(ResourceLocation dimension, @Nullable Player player, @Nullable MinecraftServer server) {
//        ADimensionRestriction serverRestriction = null;
//        ADimensionRestriction playerRestriction = null;
//
//        if (server != null) { serverRestriction = getRestriction(server, dimension); }
//        if (player != null) { playerRestriction = getRestriction(player, dimension); }
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
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.DIMENSION;
    }
}
