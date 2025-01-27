package com.alessandro.astages.networking.packet;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class StageDataSyncS2CPacket {
    private final List<String> stages;
    private final PlayerStage.Operation operation;

    public StageDataSyncS2CPacket(List<String> stages, PlayerStage.Operation operation) {
        this.stages = stages;
        this.operation = operation;
    }

    public StageDataSyncS2CPacket(@NotNull FriendlyByteBuf buf) {
        var readList = buf.readList(FriendlyByteBuf::readByteArray);
        var newStageList = new ArrayList<String>();

        for (var byteList : readList) {
            newStageList.add(new String(byteList));
        }

        this.stages = newStageList;
        this.operation = buf.readEnum(PlayerStage.Operation.class);
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        var stagesAsByte = new ArrayList<byte[]>();

        stages.forEach(stage -> {
            stagesAsByte.add(stage.getBytes());
        });

        buf.writeCollection(stagesAsByte, FriendlyByteBuf::writeByteArray);
        buf.writeEnum(operation);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            List<String> differencesBetweenClientAndServer = new ArrayList<>(ClientPlayerStage.getPlayerStages());
            differencesBetweenClientAndServer.removeAll(stages);

            List<String> differencesBetweenServerAndClient = new ArrayList<>(stages);
            differencesBetweenServerAndClient.removeAll(ClientPlayerStage.getPlayerStages());

            Set<String> differences = new HashSet<>();
            differences.addAll(differencesBetweenClientAndServer);
            differences.addAll(differencesBetweenServerAndClient);


            ClientPlayerStage.set(stages);
            MinecraftForge.EVENT_BUS.post(new ClientSynchronizeStagesEvent(new ArrayList<>(differences), operation));
        });

        ctx.get().setPacketHandled(true);
    }
}
