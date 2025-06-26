package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.AStructureRestriction;
import com.alessandro.astages.event.structure.ServerEventHandler;
import com.alessandro.astages.store.ServerStageReadable;
import com.alessandro.astages.store.server.AManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.OrderedMultiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AStructureManager extends AManager<AStructureRestriction, ResourceLocation, ResourceLocation> implements ServerStageReadable<AStructureRestriction, ResourceLocation> {
    public final OrderedMultiMap<ResourceLocation, AStructureRestriction> CACHE = OrderedMultiMap.create();

    @Override
    public void reloadBeforeScripts() {
        super.reloadBeforeScripts();
        ServerEventHandler.playerIsInStructure.clear();
        CACHE.clear();
    }

    @Override
    public void addRestriction(AStructureRestriction restriction) {
        super.addRestriction(restriction);

        for (var structure : restriction.getStructures()) {
            CACHE.put(structure, restriction);
        }
    }

    @Override
    public AStructureRestriction getRestriction(Player player, ResourceLocation structure) {
        return getRestrictionFromCache(CACHE, structure, player);
    }

    @Override
    public AStructureRestriction getRestriction(MinecraftServer server, ResourceLocation structure) {
        return getRestrictionFromCache(CACHE, structure, server);
    }

    @Override
    public AStructureRestriction getRestriction(ResourceLocation structure, @Nullable Player player, @Nullable MinecraftServer server) {
        AStructureRestriction serverRestriction = null;
        AStructureRestriction playerRestriction = null;

        if (server != null) { serverRestriction = getRestriction(server, structure); }
        if (player != null) { playerRestriction = getRestriction(player, structure); }

        if (serverRestriction == null) { // If the stage is unlocked in the server, pass!
            return null;
        }

        return playerRestriction;
    }

    @Override
    public void removeRestriction(String id) {
        super.removeRestriction(id);
        CACHE.removeValues(restriction -> restriction.getId().equals(id));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionType.STRUCTURE;
    }
}
