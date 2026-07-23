package com.alessandro.astages.infrastructure.networking.packet.reload;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.reload.ClientReloadContext;
import com.alessandro.astages.api.reload.ClientReloadPhase;
import com.alessandro.astages.engine.PluginManager;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@NotNullParams
public class RequestReloadS2C implements AStagesPacket {
    private final ClientReloadPhase reloadType;

    public RequestReloadS2C(ClientReloadPhase reloadType) {
        this.reloadType = reloadType;
    }

    public RequestReloadS2C(FriendlyByteBuf buf) {
        reloadType = buf.readEnum(ClientReloadPhase.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(reloadType);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PluginManager.callMethod(reloadType, new ClientReloadContext(), AStagesPlugin::onClientReload, AStagesPlugin::getDescriptionForClientReload);

//            switch (reloadType) {
//                case CLIENT_BEFORE -> {
//                    AClientStageManager.onReloadStarted();
//                    AClientRestrictionManager.onReloadStarted();
//                }
//                case CLIENT_SYNC -> AClientRestrictionManager.onReloadFinished();
//                case RELOAD_BEFORE -> ClientRestrictionReloadState.reloadStarted();
//                case JEI_ITEM -> ALoader.EVENT_BUS.post(new ClientItemUpdateEvent());
//                case JEI_RECIPE -> ALoader.EVENT_BUS.post(new ClientRecipeUpdateEvent());
//                case ORE -> ALoader.EVENT_BUS.post(new ClientOreUpdateEvent());
//                case ITEM -> AClientRestrictionManager.ITEM_INSTANCE.getRegistry().clearProperties();
//                case RECIPE -> AStages.LOGGER.debug("No other operations required for MarkAsDirty method for recipe restrictions!");
//            }
        });

        ctx.get().setPacketHandled(true);
    }
}
