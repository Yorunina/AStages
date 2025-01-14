package com.alessandro.astages.networking.packet.locker;

import com.alessandro.astages.AStages;
import com.alessandro.astages.util.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AreaProtectedS2CPacket implements AStagesPacket {
    public static final CustomPacketPayload.Type<AreaProtectedS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "area_protected_s2c_packet"));

    public static final StreamCodec<ByteBuf, AreaProtectedS2CPacket> STREAM_CODEC = StreamCodec.unit(new AreaProtectedS2CPacket());

    @Override
    public void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = Objects.requireNonNull(Minecraft.getInstance().player).clientLevel;
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
