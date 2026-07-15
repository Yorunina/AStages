package com.alessandro.astages.infrastructure.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.constant.ReloadType;
import com.alessandro.astages.api.event.update.ClientItemUpdateEvent;
import com.alessandro.astages.api.event.update.ClientOreUpdateEvent;
import com.alessandro.astages.api.event.update.ClientRecipeUpdateEvent;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.AClientStageManager;
import com.alessandro.astages.engine.client.ClientRestrictionReloadState;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@NotNullParams
public class RequestReloadS2C implements AStagesPacket {
    private final ReloadType reloadType;

    public RequestReloadS2C(ReloadType reloadType) {
        this.reloadType = reloadType;
    }

    public RequestReloadS2C(FriendlyByteBuf buf) {
        reloadType = buf.readEnum(ReloadType.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(reloadType);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (reloadType) {
                case CLIENT_BEFORE -> {
                    AClientStageManager.onReloadStarted();
                    AClientRestrictionManager.onReloadStarted();
                }
                case CLIENT_SYNC -> AClientRestrictionManager.onReloadFinished();
                case RELOAD_BEFORE -> ClientRestrictionReloadState.reloadStarted();
                case JEI_ITEM -> ALoader.EVENT_BUS.post(new ClientItemUpdateEvent());
                case JEI_RECIPE -> ALoader.EVENT_BUS.post(new ClientRecipeUpdateEvent());
                case ORE -> ALoader.EVENT_BUS.post(new ClientOreUpdateEvent());
                case ITEM -> AClientRestrictionManager.ITEM_INSTANCE.getRegistry().clearProperties();
                case RECIPE -> AStages.LOGGER.debug("No other operations required for MarkAsDirty method for recipe restrictions!");
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
