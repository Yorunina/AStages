package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;

@NotNullParams
public abstract class ABaseItemSyncerPacket extends RestrictionSyncerPacket {
    private final boolean renderItemName;
    private final boolean hideTooltip;
    private final boolean hideInJei;

    public ABaseItemSyncerPacket(String id, String stage, boolean renderItemName, boolean hideTooltip, boolean hideInJei) {
        super(id, stage);
        this.renderItemName = renderItemName;
        this.hideTooltip = hideTooltip;
        this.hideInJei = hideInJei;
    }

    public ABaseItemSyncerPacket(FriendlyByteBuf buf) {
        super(buf);
        renderItemName = buf.readBoolean();
        hideTooltip = buf.readBoolean();
        hideInJei = buf.readBoolean();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(renderItemName);
        buf.writeBoolean(hideTooltip);
        buf.writeBoolean(hideInJei);
    }

    public boolean isRenderItemName() {
        return renderItemName;
    }

    public boolean isHideTooltip() {
        return hideTooltip;
    }

    public boolean isHideInJei() {
        return hideInJei;
    }
}
