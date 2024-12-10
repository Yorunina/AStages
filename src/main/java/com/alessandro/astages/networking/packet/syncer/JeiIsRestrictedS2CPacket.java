package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.integration.jei.AItemStagesJEIPlugin;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class JeiIsRestrictedS2CPacket {
//    private final String stage;
    private final ItemStack stack;
//    private final AClientQuestionType type;
//    private final Component message;
//    private final boolean requestReload;

    public JeiIsRestrictedS2CPacket(ItemStack stack) {
    // public ItemIsRestrictedS2CPacket(ItemStack stack, boolean requestReload) {
    // public ItemIsRestrictedS2CPacket(ItemStack stack, Component message) {
        this.stack = stack;
//        this.message = message;
//        this.requestReload = requestReload;
    }

    public JeiIsRestrictedS2CPacket(@NotNull FriendlyByteBuf buf) {
        stack = buf.readItem();
//        requestReload = buf.readBoolean();
//        message = buf.readComponent();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeItem(stack);
//        buf.writeBoolean(requestReload);
//        buf.writeComponent(message);
    }

//
//    public ItemIsRestrictedS2CPacket(String stage, ItemStack stack, AClientQuestionType type, Component message) {
//        this.stage = stage;
//        this.stack = stack;
//        this.type = type;
//        this.message = message;
//    }
//
//    public ItemIsRestrictedS2CPacket(@NotNull FriendlyByteBuf buf) {
//        stage = buf.readUtf();
//        stack = buf.readItem();
//        type = buf.readEnum(AClientQuestionType.class);
//        message = buf.readComponent();
//    }
//
//    public void toBytes(@NotNull FriendlyByteBuf buf) {
//        buf.writeUtf(stage);
//        buf.writeItem(stack);
//        buf.writeEnum(type);
//        buf.writeComponent(message);
//    }
//
    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Here we are on client
            AItemStagesJEIPlugin.itemsToHide.add(stack);

//            if (requestReload) {
//                MinecraftForge.EVENT_BUS.post(new ClientJeiUpdateEvent());
//            }


//            if (type == AClientQuestionType.TOOLTIP) {
//                AClientRestrictionManager.ITEM_INSTANCE.restrictedStacksForTooltip.add(new Triple<>(stage, stack, message));
//            } else if (type == AClientQuestionType.NAME) {
//                AClientRestrictionManager.ITEM_INSTANCE.restrictedStacksForName.add(new Triple<>(stage, stack, message));
//            } else {
//                AItemStagesJEIPlugin.itemsToHide.add(stack);
//            }
        });

        ctx.get().setPacketHandled(true);
    }
}
