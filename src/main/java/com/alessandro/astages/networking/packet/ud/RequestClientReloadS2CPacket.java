package com.alessandro.astages.networking.packet.ud;

import com.alessandro.astages.core.client.AClientRestrictionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RequestClientReloadS2CPacket {
    public RequestClientReloadS2CPacket() { }

    public RequestClientReloadS2CPacket(FriendlyByteBuf ignoredUnused) { }

    public void toBytes(FriendlyByteBuf ignoredUnused) { }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            AClientRestrictionManager.ITEM_INSTANCE.reloadBeforeScripts();
            AClientRestrictionManager.RECIPE_INSTANCE.reloadBeforeScripts();
            AClientRestrictionManager.ORE_INSTANCE.reloadBeforeScripts();
        });

        ctx.get().setPacketHandled(true);
    }
}
