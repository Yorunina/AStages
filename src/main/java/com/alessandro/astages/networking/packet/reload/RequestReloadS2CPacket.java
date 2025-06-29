package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientItemUpdateEvent;
import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import com.alessandro.astages.util.ReloadType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class RequestReloadS2CPacket {
    private final ReloadType reloadType;

    public RequestReloadS2CPacket(ReloadType reloadType) {
        this.reloadType = reloadType;
    }

    public RequestReloadS2CPacket(FriendlyByteBuf buf) {
        reloadType = buf.readEnum(ReloadType.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(reloadType);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (reloadType) {
                case CLIENT_BEFORE -> AClientRestrictionManager.reloadBeforeScripts();
                case CLIENT_SYNC -> AClientRestrictionManager.reloadAfterScripts();
                case RELOAD_BEFORE -> AClientRestrictionManager.reloadStarted();
                case ITEM -> MinecraftForge.EVENT_BUS.post(new ClientItemUpdateEvent());
                case RECIPE -> MinecraftForge.EVENT_BUS.post(new ClientOreUpdateEvent());
                case ORE -> MinecraftForge.EVENT_BUS.post(new ClientOreUpdateEvent());
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
