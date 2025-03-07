package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.networking.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RequestClientReloadS2CPacket() implements AStagesPacket {
    public static final CustomPacketPayload.Type<RequestClientReloadS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "request_client_reload_s2c_packet"));

    public static final StreamCodec<ByteBuf, RequestClientReloadS2CPacket> STREAM_CODEC = StreamCodec.unit(new RequestClientReloadS2CPacket());

    @Override
    public void run(IPayloadContext context) {
        AClientRestrictionManager.reloadBeforeScripts();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
