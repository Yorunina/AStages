package com.alessandro.astages.networking.packet.ud;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.util.AClientQuestionType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class IsItemRestrictedC2SPacket {
    private final ItemStack stack;
//    private final AClientQuestionType type;
//    private final boolean isLastCheck;
//    private final boolean requestJeiUpdate;

    // public IsItemRestrictedC2SPacket(ItemStack stack, AClientQuestionType type, boolean isLastCheck) {
    public IsItemRestrictedC2SPacket(ItemStack stack) {
        this.stack = stack;
//        this.type = type;
//        this.isLastCheck = isLastCheck;
    }

    public IsItemRestrictedC2SPacket(@NotNull FriendlyByteBuf buf) {
        stack = buf.readItem();
//        type = buf.readEnum(AClientQuestionType.class);
//        isLastCheck = buf.readBoolean();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeItem(stack);
//        buf.writeEnum(type);
//        buf.writeBoolean(isLastCheck);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Here we are on server
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(stack);

            if (restriction != null) {
                ModNetworking.sendToPlayer(new ItemSyncerS2CPacket(restriction.id, restriction.stage, stack, restriction.renderItemName, restriction.hideTooltip, restriction.getHiddenName(stack), restriction.getJadeItemMessage(stack), restriction.getJadeBlockMessage(stack)), ctx.get().getSender());
            }

//            if (type == AClientQuestionType.TOOLTIP) {
//                if (restriction != null && restriction.hideTooltip) {
//                    ModNetworking.sendToPlayer(new ItemIsRestrictedS2CPacket(restriction.stage, stack, AClientQuestionType.TOOLTIP, restriction.getHiddenName(stack)), ctx.get().getSender());
//                } else {
//                    ModNetworking.sendToPlayer(new ItemIsNotRestrictedS2CPacket(stack, AClientQuestionType.TOOLTIP), ctx.get().getSender());
//                }
//            } else if (type == AClientQuestionType.NAME) {
//                if (restriction != null && !restriction.renderItemName) {
//                    ModNetworking.sendToPlayer(new ItemIsRestrictedS2CPacket(restriction.stage, stack, AClientQuestionType.NAME, restriction.getHiddenName(stack)), ctx.get().getSender());
//                } else {
//                    ModNetworking.sendToPlayer(new ItemIsNotRestrictedS2CPacket(stack, AClientQuestionType.NAME), ctx.get().getSender());
//                }
//            } else {
//                if (restriction != null && restriction.hideInJEI) {
//                    ModNetworking.sendToPlayer(new ItemIsRestrictedS2CPacket(restriction.stage, stack, AClientQuestionType.JEI, Component.empty()), ctx.get().getSender());
//                }
//                else {
//                    ModNetworking.sendToPlayer(new ItemIsNotRestrictedS2CPacket(stack, AClientQuestionType.JEI), ctx.get().getSender());
//                }
//            }

//            if (isLastCheck && type == AClientQuestionType.JEI) {
//                ModNetworking.sendToPlayer(new JeiSyncerS2CPacket(), ctx.get().getSender());
//            }
        });

        ctx.get().setPacketHandled(true);
    }
}
