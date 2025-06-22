package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientItemUpdateEvent;
import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.store.ReloadType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@MethodsReturnNonnullByDefault
public record RequestReloadS2CPacket(ReloadType reloadType) implements AStagesPacket {
    public static final CustomPacketPayload.Type<RequestReloadS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "request_reload_s2c_packet"));

    public static final StreamCodec<FriendlyByteBuf, RequestReloadS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.idMapper(ReloadType.BY_ID, ReloadType::getId),
        RequestReloadS2CPacket::reloadType,
        RequestReloadS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (reloadType) {
            case CLIENT_BEFORE -> AClientRestrictionManager.reloadBeforeScripts();
            case ITEM -> {
                AClientRestrictionManager.setWaitingForItemUpdate(true);
                NeoForge.EVENT_BUS.post(new ClientItemUpdateEvent());
            }
            case RECIPE -> {
                AClientRestrictionManager.setWaitingForRecipeUpdate(true);
                NeoForge.EVENT_BUS.post(new ClientRecipeUpdateEvent());
            }
            case ORE -> NeoForge.EVENT_BUS.post(new ClientOreUpdateEvent());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
