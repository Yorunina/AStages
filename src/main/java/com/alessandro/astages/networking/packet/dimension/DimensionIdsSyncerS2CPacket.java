package com.alessandro.astages.networking.packet.dimension;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record DimensionIdsSyncerS2CPacket(List<String> ids) implements AStagesPacket {
    public static final CustomPacketPayload.Type<DimensionIdsSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "dimension_ids_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, DimensionIdsSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), DimensionIdsSyncerS2CPacket::ids,
        DimensionIdsSyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        AClientRestrictionManager.DIMENSION_IDS.clear();
        AClientRestrictionManager.DIMENSION_IDS.addAll(ids);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
