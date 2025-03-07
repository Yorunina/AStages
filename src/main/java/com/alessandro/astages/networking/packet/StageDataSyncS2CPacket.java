package com.alessandro.astages.networking.packet;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.networking.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record StageDataSyncS2CPacket(List<String> stages, PlayerStage.Operation operation) implements AStagesPacket {
    public static final CustomPacketPayload.Type<StageDataSyncS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "stage_data_sync_s2c_packet"));

    public static final StreamCodec<ByteBuf, StageDataSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), StageDataSyncS2CPacket::stages,
        ByteBufCodecs.idMapper(PlayerStage.Operation.BY_ID, PlayerStage.Operation::id), StageDataSyncS2CPacket::operation,
        StageDataSyncS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        // HERE WE ARE ON CLIENT!
        List<String> differencesBetweenClientAndServer = new ArrayList<>(ClientPlayerStage.getPlayerStages());
        differencesBetweenClientAndServer.removeAll(stages);

        List<String> differencesBetweenServerAndClient = new ArrayList<>(stages);
        differencesBetweenServerAndClient.removeAll(ClientPlayerStage.getPlayerStages());

        Set<String> differences = new HashSet<>();
        differences.addAll(differencesBetweenClientAndServer);
        differences.addAll(differencesBetweenServerAndClient);


        ClientPlayerStage.set(stages);
        NeoForge.EVENT_BUS.post(new ClientSynchronizeStagesEvent(new ArrayList<>(differences), operation));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
