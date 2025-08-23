package com.alessandro.astages.networking.packet;

import com.alessandro.astages.api.annotation.nullability.NotNullParams;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.google.common.collect.Sets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@NotNullParams
public class StageDataSyncS2CPacket {
    private final List<String> stages;
    private final PlayerStage.Operation operation;

    public StageDataSyncS2CPacket(List<String> stages, PlayerStage.Operation operation) {
        this.stages = stages;
        this.operation = operation;
    }

    public StageDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.stages = buf.readList(FriendlyByteBuf::readUtf);
        this.operation = buf.readEnum(PlayerStage.Operation.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
        buf.writeEnum(operation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            List<String> differencesBetweenClientAndServer = new ArrayList<>(ClientPlayerStage.getPlayerStages());
            differencesBetweenClientAndServer.removeAll(stages);

            List<String> differencesBetweenServerAndClient = new ArrayList<>(stages);
            differencesBetweenServerAndClient.removeAll(ClientPlayerStage.getPlayerStages());

            Set<String> differences = new HashSet<>();
            differences.addAll(differencesBetweenClientAndServer);
            differences.addAll(differencesBetweenServerAndClient);

            var synchronizedStages = Sets.symmetricDifference(new HashSet<>(ClientPlayerStage.getPlayerStages()), new HashSet<>(stages));

            ClientPlayerStage.set(stages);
            MinecraftForge.EVENT_BUS.post(new ClientSynchronizeStagesEvent(new ArrayList<>(differences), operation));
        });

        ctx.get().setPacketHandled(true);
    }
}
