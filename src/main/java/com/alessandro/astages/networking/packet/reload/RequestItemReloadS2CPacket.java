package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientItemUpdateEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RequestItemReloadS2CPacket {
    public RequestItemReloadS2CPacket() { }

    public RequestItemReloadS2CPacket(FriendlyByteBuf ignoredBuf) { }

    public void toBytes(FriendlyByteBuf ignoredBuf) { }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AClientRestrictionManager.waitingForItemUpdate = true;
            MinecraftForge.EVENT_BUS.post(new ClientItemUpdateEvent());
        });

        ctx.get().setPacketHandled(true);
    }
}
