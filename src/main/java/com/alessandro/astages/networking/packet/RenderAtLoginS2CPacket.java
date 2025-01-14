package com.alessandro.astages.networking.packet;

import com.alessandro.astages.AStages;
import com.alessandro.astages.event.ore.ClientEventHandler;
import com.alessandro.astages.util.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RenderAtLoginS2CPacket() implements AStagesPacket {
    public static final CustomPacketPayload.Type<RenderAtLoginS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "render_at_login_s2c_packet"));

    public static final StreamCodec<ByteBuf, RenderAtLoginS2CPacket> STREAM_CODEC = StreamCodec.unit(new RenderAtLoginS2CPacket());

    @Override
    public void handle(@NotNull IPayloadContext context) {
        context.enqueueWork((ClientEventHandler::renderAllAgain)).exceptionally(e -> {
            AStages.LOGGER.debug(e.getLocalizedMessage());
            return null;
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
