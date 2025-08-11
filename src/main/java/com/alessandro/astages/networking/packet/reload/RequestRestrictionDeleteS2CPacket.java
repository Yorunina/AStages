package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.AStagesUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RequestRestrictionDeleteS2CPacket(String id, ARestrictionType restrictionType) implements AStagesPacket {
    public static final CustomPacketPayload.Type<RequestRestrictionDeleteS2CPacket> TYPE = new CustomPacketPayload.Type<>(AStagesUtil.fromNamespaceAndPath("request_restriction_delete_s2c_packet"));

    public static final StreamCodec<ByteBuf, RequestRestrictionDeleteS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, RequestRestrictionDeleteS2CPacket::id,
            ByteBufCodecs.idMapper(ARestrictionType.BY_ID, ARestrictionType::getId), RequestRestrictionDeleteS2CPacket::restrictionType,
            RequestRestrictionDeleteS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        AClientRestrictionManager.removeRestriction(id, restrictionType);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
