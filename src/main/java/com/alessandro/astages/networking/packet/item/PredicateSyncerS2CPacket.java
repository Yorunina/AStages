package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.core.client.item.AClientPredicateRestriction;
import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PredicateSyncerS2CPacket extends RestrictionSyncerPacket {
    private final ResourceLocation modelId;
    private final boolean hideInJei;

    public PredicateSyncerS2CPacket(String id, String stage, ResourceLocation modelId, boolean hideInJei) {
        super(id, stage);
        this.modelId = modelId;
        this.hideInJei = hideInJei;
    }

    public PredicateSyncerS2CPacket(FriendlyByteBuf buf) {
        super(buf);
        modelId = buf.readResourceLocation();
        hideInJei = buf.readBoolean();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeResourceLocation(modelId);
        buf.writeBoolean(hideInJei);
    }

    @Override
    public void handle() {
        var restriction = new AClientPredicateRestriction(getId(), getStage()).setHideInJei(hideInJei);
        restriction.restrict(modelId);
        AClientRestrictionManager.NEW_ITEM_INSTANCE.addRestriction(restriction);
    }
}
