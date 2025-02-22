package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.core.client.old.AClientItemRestriction;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Info("To be deleted!")
public class ItemSyncerS2CPacket {
    private final String id;
    private final String stage;
    private final ItemStack stack;
    private final boolean renderItemName;
    private final boolean hideTooltip;
    private final Component tooltipMessage;
    private final Component jadeItemMessage;
    private final Component jadeBlockMessage;

    public ItemSyncerS2CPacket(String id, String stage, ItemStack stack, boolean renderItemName, boolean hideTooltip, Component tooltipMessage, Component jadeItemMessage, Component jadeBlockMessage) {
        this.id = id;
        this.stage = stage;
        this.stack = stack;
        this.renderItemName = renderItemName;
        this.hideTooltip = hideTooltip;
        this.tooltipMessage = tooltipMessage;
        this.jadeItemMessage = jadeItemMessage;
        this.jadeBlockMessage = jadeBlockMessage;
    }

    public ItemSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        id = buf.readUtf();
        stage = buf.readUtf();
        stack = buf.readItem();
        renderItemName = buf.readBoolean();
        hideTooltip = buf.readBoolean();
        tooltipMessage = buf.readComponent();
        jadeItemMessage = buf.readComponent();
        jadeBlockMessage = buf.readComponent();
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
            // HERE WE ARE ON CLIENT!
            var restriction = new AClientItemRestriction(id, stage, stack, renderItemName, hideTooltip, tooltipMessage, jadeItemMessage, jadeBlockMessage);

//            AClientRestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);
        });

        ctx.get().setPacketHandled(true);
    }
}
