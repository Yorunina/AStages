package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.alessandro.astages.networking.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RequestRecipeReloadS2CPacket() implements AStagesPacket {
    public static final CustomPacketPayload.Type<RequestRecipeReloadS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "request_recipe_reload_s2c_packet"));

    public static final StreamCodec<ByteBuf, RequestRecipeReloadS2CPacket> STREAM_CODEC = StreamCodec.unit(new RequestRecipeReloadS2CPacket());


    @Override
    public void run(IPayloadContext context) {
        AClientRestrictionManager.setWaitingForRecipeUpdate(true);
        NeoForge.EVENT_BUS.post(new ClientRecipeUpdateEvent());
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
