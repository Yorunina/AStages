package com.alessandro.astages.networking.packet;

import com.alessandro.astages.event.ore.ClientEventHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RenderAtLoginS2CPacket {
    public RenderAtLoginS2CPacket() { }

    @SuppressWarnings("unused")
    public RenderAtLoginS2CPacket(@NotNull FriendlyByteBuf buf) { }

    @SuppressWarnings("unused")
    public void toBytes(@NotNull FriendlyByteBuf buf) { }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(ClientEventHandler::renderAllAgain);

        ctx.get().setPacketHandled(true);
    }
}
