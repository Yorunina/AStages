package com.alessandro.astages.networking.packet.ud;

import com.alessandro.astages.event.custom.actions.ClientJeiUpdateEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class JeiSyncerS2CPacket {
    public JeiSyncerS2CPacket() { }

    public JeiSyncerS2CPacket(FriendlyByteBuf buf) { }

    public void toBytes(FriendlyByteBuf buf) { }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> MinecraftForge.EVENT_BUS.post(new ClientJeiUpdateEvent()));

        ctx.get().setPacketHandled(true);
    }
}
