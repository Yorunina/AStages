package com.alessandro.astages.networking.packet;

import com.alessandro.astages.Astages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.networking.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record StageData(List<String> stages) implements AStagesPacket {
    public static final CustomPacketPayload.Type<StageData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Astages.MODID, "stage_data_server_to_client"));

    public static final StreamCodec<ByteBuf, StageData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
        StageData::stages,
        StageData::new
    );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handle(@NotNull IPayloadContext context) {
        // HERE WE ARE ON CLIENT!
        ClientPlayerStage.set(this.stages);
    }
}
