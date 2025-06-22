package com.alessandro.astages.networking.packet.server;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@MethodsReturnNonnullByDefault
public record ServerStagesSyncerS2CPacket(List<String> stages) implements AStagesPacket {
    public static final CustomPacketPayload.Type<ServerStagesSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "server_stages_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerStagesSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), ServerStagesSyncerS2CPacket::stages,
        ServerStagesSyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        // HERE WE ARE ON CLIENT!
        AClientRestrictionManager.SERVER_STAGES.clear();
        AClientRestrictionManager.SERVER_STAGES.addAll(stages);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
