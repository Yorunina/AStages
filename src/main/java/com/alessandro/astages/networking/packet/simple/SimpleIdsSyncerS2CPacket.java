package com.alessandro.astages.networking.packet.simple;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.util.SyncOperation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@MethodsReturnNonnullByDefault
public record SimpleIdsSyncerS2CPacket(List<String> ids, SyncOperation operation) implements AStagesPacket {
    public static final CustomPacketPayload.Type<SimpleIdsSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "simple_stages_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SimpleIdsSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), SimpleIdsSyncerS2CPacket::ids,
            ByteBufCodecs.idMapper(SyncOperation.BY_ID, SyncOperation::getId), SimpleIdsSyncerS2CPacket::operation,
            SimpleIdsSyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (operation) {
            case ADD -> AClientRestrictionManager.SIMPLE_IDS.addAll(ids);
            case REMOVE -> ids.forEach(AClientRestrictionManager.SIMPLE_IDS::remove);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
