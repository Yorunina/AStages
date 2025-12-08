package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.AClientStageManager;
import com.alessandro.astages.event.custom.actions.ClientItemUpdateEvent;
import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.util.ReloadType;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@NotNullParams
public class RequestReloadS2CPacket implements AStagesPacket {
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
                case CLIENT_BEFORE -> {
                    AClientStageManager.reloadBeforeScripts();
                    AClientRestrictionManager.reloadBeforeScripts();
                }
                case CLIENT_SYNC -> AClientRestrictionManager.reloadAfterScripts();
                case RELOAD_BEFORE -> AClientRestrictionManager.reloadStarted();
                case JEI_ITEM -> MinecraftForge.EVENT_BUS.post(new ClientItemUpdateEvent());
                case JEI_RECIPE -> MinecraftForge.EVENT_BUS.post(new ClientRecipeUpdateEvent());
                case ORE -> MinecraftForge.EVENT_BUS.post(new ClientOreUpdateEvent());
                case ITEM -> AClientRestrictionManager.ITEM_INSTANCE.clearProperties();
                case RECIPE -> AStages.LOGGER.debug("No other operations required for MarkAsDirty method for recipe restrictions!");
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
