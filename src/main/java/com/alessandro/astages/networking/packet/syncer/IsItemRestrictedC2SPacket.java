package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.util.develop.Info;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Info("To be deleted!")
public class IsItemRestrictedC2SPacket {
    private final ItemStack stack;

    public IsItemRestrictedC2SPacket(ItemStack stack) {
        this.stack = stack;
    }

    public IsItemRestrictedC2SPacket(@NotNull FriendlyByteBuf buf) {
        stack = buf.readItem();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeItem(stack);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON SERVER
//            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(stack); // Regardless to player...
//
//            if (restriction != null) {
//                ModNetworking.sendToPlayer(new ItemSyncerS2CPacket(restriction.getId(), restriction.getStage(), stack, restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.getMessage(Attributes.Item.HIDDEN_NAME, stack), restriction.getMessage(Attributes.Item.JADE_ITEM_MESSAGE, stack), restriction.getMessage(Attributes.Item.JADE_BLOCK_MESSAGE, stack)), ctx.get().getSender());
//            } else {
//                ModNetworking.sendToPlayer(new NullItemSyncerS2CPacket(stack), ctx.get().getSender());
//            }
        });

        ctx.get().setPacketHandled(true);
    }
}
