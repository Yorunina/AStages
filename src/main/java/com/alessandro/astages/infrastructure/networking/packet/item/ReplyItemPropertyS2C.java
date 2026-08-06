package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemPropertyRestriction;
import com.alessandro.astages.engine.server.restriction.item.ABaseItemRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@NotNullParamsAndMethodsReturn
public class ReplyItemPropertyS2C implements AStagesPacket {
    private final String id;
    private final String stage;
    private final ItemStack stack;
    private final Component actionBarMessage;
    private final Component tooltipMessage;
    private final Component recipeViewerMessage;
    private final Component jadeItemMessage;
    private final Component jadeBlockMessage;

    public ReplyItemPropertyS2C(String id, String stage, ItemStack stack, Component actionBarMessage, Component tooltipMessage, Component recipeViewerMessage, Component jadeItemMessage, Component jadeBlockMessage) {
        this.id = id;
        this.stage = stage;
        this.stack = stack;
        this.actionBarMessage = actionBarMessage;
        this.tooltipMessage = tooltipMessage;
        this.recipeViewerMessage = recipeViewerMessage;
        this.jadeItemMessage = jadeItemMessage;
        this.jadeBlockMessage = jadeBlockMessage;
    }

    public ReplyItemPropertyS2C(ABaseItemRestriction<?, ?> restriction, ItemStack stack) {
        this(restriction.getId(), restriction.getStage(), stack,
            restriction.get(Attributes.Item.ACTION_BAR_MESSAGE).apply(stack),
            restriction.get(Attributes.Item.TOOLTIP_MESSAGE).apply(stack),
            restriction.get(Attributes.Item.RECIPE_VIEWER_MESSAGE).apply(stack),
            restriction.get(Attributes.Item.JADE_ITEM_MESSAGE).apply(stack),
            restriction.get(Attributes.Item.JADE_BLOCK_MESSAGE).apply(stack));
    }

    public ReplyItemPropertyS2C(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.stage = buf.readUtf();
        this.stack = buf.readItem();
        this.actionBarMessage = buf.readComponent();
        this.tooltipMessage = buf.readComponent();
        this.recipeViewerMessage = buf.readComponent();
        this.jadeItemMessage = buf.readComponent();
        this.jadeBlockMessage = buf.readComponent();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
        buf.writeItem(stack);
        buf.writeComponent(actionBarMessage);
        buf.writeComponent(tooltipMessage);
        buf.writeComponent(recipeViewerMessage);
        buf.writeComponent(jadeItemMessage);
        buf.writeComponent(jadeBlockMessage);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var restriction = new AClientItemPropertyRestriction(id, stage, stack)
                .set(Attributes.Item.ACTION_BAR_MESSAGE, s -> actionBarMessage)
                .set(Attributes.Item.TOOLTIP_MESSAGE, s -> tooltipMessage)
                .set(Attributes.Item.RECIPE_VIEWER_MESSAGE, s -> recipeViewerMessage)
                .set(Attributes.Item.JADE_ITEM_MESSAGE, s -> jadeItemMessage)
                .set(Attributes.Item.JADE_BLOCK_MESSAGE, s -> jadeBlockMessage);
            
            AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
        });

        ctx.get().setPacketHandled(true);
    }
}
