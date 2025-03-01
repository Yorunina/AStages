package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.core.client.item.AClientItemPropertyRestriction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ItemPropertySyncerS2CPacket {
    private final String id;
    private final String stage;
    private final ItemStack stack;
    private final boolean renderItemName;
    private final boolean hideTooltip;
    private final Component tooltipMessage;
    private final Component jadeItemMessage;
    private final Component jadeBlockMessage;

    public ItemPropertySyncerS2CPacket(String id, String stage, ItemStack stack, boolean renderItemName, boolean hideTooltip, Component tooltipMessage, Component jadeItemMessage, Component jadeBlockMessage) {
        this.id = id;
        this.stage = stage;
        this.stack = stack;
        this.renderItemName = renderItemName;
        this.hideTooltip = hideTooltip;
        this.tooltipMessage = tooltipMessage;
        this.jadeItemMessage = jadeItemMessage;
        this.jadeBlockMessage = jadeBlockMessage;
    }

    public ItemPropertySyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.stage = buf.readUtf();
        this.stack = buf.readItem();
        this.renderItemName = buf.readBoolean();
        this.hideTooltip = buf.readBoolean();
        this.tooltipMessage = buf.readComponent();
        this.jadeItemMessage = buf.readComponent();
        this.jadeBlockMessage = buf.readComponent();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
        buf.writeItem(stack);
        buf.writeBoolean(renderItemName);
        buf.writeBoolean(hideTooltip);
        buf.writeComponent(tooltipMessage);
        buf.writeComponent(jadeItemMessage);
        buf.writeComponent(jadeBlockMessage);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var restriction = new AClientItemPropertyRestriction(id, stage, stack, renderItemName, hideTooltip, tooltipMessage, jadeItemMessage, jadeBlockMessage);
            AClientRestrictionManager.NEW_ITEM_INSTANCE.addRestriction(restriction);
        });

        ctx.get().setPacketHandled(true);
    }
}
