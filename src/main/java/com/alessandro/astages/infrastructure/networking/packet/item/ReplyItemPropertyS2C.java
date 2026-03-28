package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemPropertyRestriction;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@NotNullParams
public class ReplyItemPropertyS2C implements AStagesPacket {
    private final String id;
    private final String stage;
    private final ItemStack stack;
    private final Component hiddenName;
    private final Component jadeItemMessage;
    private final Component jadeBlockMessage;

    public ReplyItemPropertyS2C(String id, String stage, ItemStack stack, Component hiddenName, Component jadeItemMessage, Component jadeBlockMessage) {
        this.id = id;
        this.stage = stage;
        this.stack = stack;
        this.hiddenName = hiddenName;
        this.jadeItemMessage = jadeItemMessage;
        this.jadeBlockMessage = jadeBlockMessage;
    }

    public ReplyItemPropertyS2C(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.stage = buf.readUtf();
        this.stack = buf.readItem();
        this.hiddenName = buf.readComponent();
        this.jadeItemMessage = buf.readComponent();
        this.jadeBlockMessage = buf.readComponent();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
        buf.writeItem(stack);
        buf.writeComponent(hiddenName);
        buf.writeComponent(jadeItemMessage);
        buf.writeComponent(jadeBlockMessage);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var restriction = new AClientItemPropertyRestriction(id, stage, stack, hiddenName, jadeItemMessage, jadeBlockMessage);
            AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        });

        ctx.get().setPacketHandled(true);
    }
}
