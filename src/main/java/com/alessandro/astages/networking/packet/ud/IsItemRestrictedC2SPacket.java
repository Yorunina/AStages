package com.alessandro.astages.networking.packet.ud;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.networking.ModNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

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
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(stack); // Regardless to player...

            if (restriction != null) {
                ModNetworking.sendToPlayer(new ItemSyncerS2CPacket(restriction.id, restriction.stage, stack, restriction.renderItemName, restriction.hideTooltip, restriction.getHiddenName(stack), restriction.getJadeItemMessage(stack), restriction.getJadeBlockMessage(stack)), ctx.get().getSender());
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
