package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.core.AClientRestrictionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RequestClientReloadS2CPacket {
    public RequestClientReloadS2CPacket() { }

    public RequestClientReloadS2CPacket(FriendlyByteBuf ignoredUnused) { }

    public void toBytes(FriendlyByteBuf ignoredUnused) { }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(AClientRestrictionManager::reloadBeforeScripts);

        ctx.get().setPacketHandled(true);
    }
}
