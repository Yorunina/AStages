package com.alessandro.astages.networking.packet.ud;

import com.alessandro.astages.core.client.AClientRestrictionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Supplier;

public class RequestClientReloadS2CPacket {
    public RequestClientReloadS2CPacket() { }

    @SuppressWarnings("unused")
    public RequestClientReloadS2CPacket(FriendlyByteBuf unused) { }

    @SuppressWarnings("unused")
    public void toBytes(FriendlyByteBuf unused) { }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AClientRestrictionManager.RECIPE_INSTANCE.restrictions = new HashMap<>();
            AClientRestrictionManager.ORE_INSTANCE.restrictions = new HashMap<>();
        });

        ctx.get().setPacketHandled(true);
    }
}
