package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.event.custom.actions.ClientJeiReloadEvent;
import com.alessandro.astages.util.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RequestJeiClientReloadS2CPacket() implements AStagesPacket {
    public static final Type<RequestJeiClientReloadS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "request_jei_client_reload_s2c_packet"));

    public static final StreamCodec<ByteBuf, RequestJeiClientReloadS2CPacket> STREAM_CODEC = StreamCodec.unit(new RequestJeiClientReloadS2CPacket());

    @Override
    public void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> {
            NeoForge.EVENT_BUS.post(new ClientJeiReloadEvent());
        }).exceptionally(e -> {
            AStages.LOGGER.debug(e.getLocalizedMessage());
            return null;
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
