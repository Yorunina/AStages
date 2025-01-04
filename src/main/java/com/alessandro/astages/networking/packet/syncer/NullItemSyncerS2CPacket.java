package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.core.client.AClientItemRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class NullItemSyncerS2CPacket {
    private final ItemStack stack;

    public NullItemSyncerS2CPacket(ItemStack stack) {
        this.stack = stack;
    }

    public NullItemSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        stack = buf.readItem();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeItem(stack);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AClientRestrictionManager.ITEM_INSTANCE.notRestricted(stack);
        });

        ctx.get().setPacketHandled(true);
    }
}
