package com.alessandro.astages.networking.packet;

import com.alessandro.astages.Astages;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record StageDataSyncS2CPacket(List<String> stages) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StageDataSyncS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Astages.MODID, "stage_data_server_to_client"));

    public static final StreamCodec<ByteBuf, StageDataSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
        StageDataSyncS2CPacket::stages,
        StageDataSyncS2CPacket::new
    );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
