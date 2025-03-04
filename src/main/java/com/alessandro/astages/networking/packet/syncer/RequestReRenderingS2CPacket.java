package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RequestReRenderingS2CPacket {
    public RequestReRenderingS2CPacket() { }

    @SuppressWarnings("unused")
    public RequestReRenderingS2CPacket(@NotNull FriendlyByteBuf buf) { }

    @SuppressWarnings("unused")
    public void toBytes(@NotNull FriendlyByteBuf buf) { }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        // ctx.get().enqueueWork(ClientEventHandler::renderAllAgain);
        ctx.get().enqueueWork(() -> {
            MinecraftForge.EVENT_BUS.post(new ClientOreUpdateEvent());
        });

        ctx.get().setPacketHandled(true);
    }
}
