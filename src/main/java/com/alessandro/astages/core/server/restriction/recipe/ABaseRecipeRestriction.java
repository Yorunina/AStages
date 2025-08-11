package com.alessandro.astages.core.server.restriction.recipe;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.store.AMarkable;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.util.ReloadType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class ABaseRecipeRestriction<R extends ARestriction<R, U, V>, U, V> extends ARestriction<R, U, V> implements AMarkable {
    public ABaseRecipeRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder();

        var pluginAttributes = ARestrictionManager.ATTACHED_ATTRIBUTES.getOrDefault(ABaseRecipeRestriction.class, null);

        if (pluginAttributes != null) {
            return defaultAttributes.combineWith(pluginAttributes);
        } else {
            return defaultAttributes;
        }
    }

    @Override
    public R restrict(U object) {
        return null;
    }

    @Override
    public boolean isRestricted(V object) {
        return false;
    }

    @Override
    public void markAsDirty() {
        PacketDistributor.sendToAllPlayers(new RequestReloadS2CPacket(ReloadType.JEI_RECIPE));
    }
}
