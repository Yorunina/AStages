package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public abstract class BaseSyncerPacket extends RestrictionSyncerPacket {
    private final boolean renderItemName;
    private final boolean hideTooltip;
    private final Component tooltipMessage;
    private final Component jadeItemMessage;
    private final Component jadeBlockMessage;

    public BaseSyncerPacket(String id, String stage, boolean renderItemName, boolean hideTooltip, Component tooltipMessage, Component jadeItemMessage, Component jadeBlockMessage) {
        super(id, stage);
        this.renderItemName = renderItemName;
        this.hideTooltip = hideTooltip;
        this.tooltipMessage = tooltipMessage;
        this.jadeItemMessage = jadeItemMessage;
        this.jadeBlockMessage = jadeBlockMessage;
    }

    public BaseSyncerPacket(FriendlyByteBuf buf) {
        super(buf);
        this.renderItemName = buf.readBoolean();
        this.hideTooltip = buf.readBoolean();
        this.tooltipMessage = buf.readComponent();
        this.jadeItemMessage = buf.readComponent();
        this.jadeBlockMessage = buf.readComponent();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
    }
}
