package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.event.custom.actions.ClientJeiReloadEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RequestJeiClientReloadS2CPacket {
    public RequestJeiClientReloadS2CPacket() { }

    public RequestJeiClientReloadS2CPacket(FriendlyByteBuf ignoredBuf) { }

    public void toBytes(FriendlyByteBuf ignoredBuf) { }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> MinecraftForge.EVENT_BUS.post(new ClientJeiReloadEvent()));

        ctx.get().setPacketHandled(true);
    }
}
