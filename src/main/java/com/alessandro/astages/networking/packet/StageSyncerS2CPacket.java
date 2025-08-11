package com.alessandro.astages.networking.packet;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.SyncOperation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@MethodsReturnNonnullByDefault
public record StageSyncerS2CPacket(List<String> stages, SyncOperation operation) implements AStagesPacket {
    public static final CustomPacketPayload.Type<StageSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(AStagesUtil.fromNamespaceAndPath("stages_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, StageSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), StageSyncerS2CPacket::stages,
            ByteBufCodecs.idMapper(SyncOperation.BY_ID, SyncOperation::getId), StageSyncerS2CPacket::operation,
            StageSyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        switch (operation) {
            case ADD -> AClientRestrictionManager.ALL_STAGES.addAll(stages);
            case REMOVE -> stages.forEach(AClientRestrictionManager.ALL_STAGES::remove);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}