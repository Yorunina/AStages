package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientItemUpdateEvent;
import com.alessandro.astages.networking.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RequestItemReloadS2CPacket() implements AStagesPacket {
    public static final CustomPacketPayload.Type<RequestItemReloadS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "request_item_reload_s2c_packet"));

    public static final StreamCodec<ByteBuf, RequestItemReloadS2CPacket> STREAM_CODEC = StreamCodec.unit(new RequestItemReloadS2CPacket());

    @Override
    public void run(IPayloadContext context) {
        AClientRestrictionManager.waitingForItemUpdate = true;
        NeoForge.EVENT_BUS.post(new ClientItemUpdateEvent());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
