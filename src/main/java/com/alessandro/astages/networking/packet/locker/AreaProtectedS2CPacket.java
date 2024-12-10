package com.alessandro.astages.networking.packet.locker;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AreaProtectedS2CPacket {

    public AreaProtectedS2CPacket() {

    }

    public AreaProtectedS2CPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!

            var level = Minecraft.getInstance().player.clientLevel;
        });

        ctx.get().setPacketHandled(true);
    }
}
