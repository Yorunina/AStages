package com.alessandro.astages.networking.packet.ud;

import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.util.AClientQuestionType;
import com.alessandro.astages.util.Triple;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ItemIsNotRestrictedS2CPacket {
//    private final ItemStack stack;
//    private final AClientQuestionType type;
//
//    public ItemIsNotRestrictedS2CPacket(ItemStack stack, AClientQuestionType type) {
//        this.stack = stack;
//        this.type = type;
//    }
//
//    public ItemIsNotRestrictedS2CPacket(@NotNull FriendlyByteBuf buf) {
//        stack = buf.readItem();
//        type = buf.readEnum(AClientQuestionType.class);
//    }
//
//    public void toBytes(@NotNull FriendlyByteBuf buf) {
//        buf.writeItem(stack);
//        buf.writeEnum(type);
//    }
//
//    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
//        ctx.get().enqueueWork(() -> {
//            if (type == AClientQuestionType.TOOLTIP) {
//                AClientRestrictionManager.ITEM_INSTANCE.notRestrictedStacksForTooltip.add(stack);
//            } else {
//                AClientRestrictionManager.ITEM_INSTANCE.notRestrictedStacksForName.add(stack);
//            }
//        });
//
//        ctx.get().setPacketHandled(true);
//    }
}
