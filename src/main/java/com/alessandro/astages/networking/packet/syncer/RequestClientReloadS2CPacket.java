package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.util.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RequestClientReloadS2CPacket() implements AStagesPacket {
    public static final Type<RequestClientReloadS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "request_client_reload_s2c_packet"));

    public static final StreamCodec<ByteBuf, RequestClientReloadS2CPacket> STREAM_CODEC = StreamCodec.unit(new RequestClientReloadS2CPacket());

    @Override
    public void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> {
            AClientRestrictionManager.ITEM_INSTANCE.reloadBeforeScripts();
            AClientRestrictionManager.RECIPE_INSTANCE.reloadBeforeScripts();
            AClientRestrictionManager.ORE_INSTANCE.reloadBeforeScripts();

            AClientRestrictionManager.ORE_STAGES.clear();
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
