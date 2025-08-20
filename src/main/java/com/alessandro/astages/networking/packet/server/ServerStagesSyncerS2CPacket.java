package com.alessandro.astages.networking.packet.server;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.ClientSynchronizeServerStagesEvent;
import com.alessandro.astages.util.annotations.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@NotNullParams
public class ServerStagesSyncerS2CPacket {
    private final List<String> stages;
    private final PlayerStage.Operation operation;

    public ServerStagesSyncerS2CPacket(List<String> stages, PlayerStage.Operation operation) {
        this.stages = stages;
        this.operation = operation;
    }

    public ServerStagesSyncerS2CPacket(FriendlyByteBuf buf) {
        stages = buf.readList(FriendlyByteBuf::readUtf);
        operation = buf.readEnum(PlayerStage.Operation.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
        buf.writeEnum(operation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            List<String> differencesBetweenClientAndServer = new ArrayList<>(AClientRestrictionManager.SERVER_STAGES);
            differencesBetweenClientAndServer.removeAll(stages);

            List<String> differencesBetweenServerAndClient = new ArrayList<>(stages);
            differencesBetweenServerAndClient.removeAll(AClientRestrictionManager.SERVER_STAGES);

            Set<String> differences = new HashSet<>();
            differences.addAll(differencesBetweenClientAndServer);
            differences.addAll(differencesBetweenServerAndClient);

            MinecraftForge.EVENT_BUS.post(new ClientSynchronizeServerStagesEvent(new ArrayList<>(differences), operation));

            AClientRestrictionManager.SERVER_STAGES.clear();
            AClientRestrictionManager.SERVER_STAGES.addAll(stages);
        });

        ctx.get().setPacketHandled(true);
    }
}
