package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.integration.jei.AItemStagesJEIPlugin;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class JeiIsRestrictedS2CPacket {
    private final ItemStack stack;

    public JeiIsRestrictedS2CPacket(ItemStack stack) {
        this.stack = stack;
    }

    public JeiIsRestrictedS2CPacket(@NotNull FriendlyByteBuf buf) {
        stack = buf.readItem();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeItem(stack);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Here we are on client
            AItemStagesJEIPlugin.itemsToHide.add(stack);
        });

        ctx.get().setPacketHandled(true);
    }
}
