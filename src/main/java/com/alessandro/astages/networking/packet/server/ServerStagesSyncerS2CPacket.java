package com.alessandro.astages.networking.packet.server;

import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.event.custom.ClientSynchronizeServerStagesEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

@NotNullParams
public class ServerStagesSyncerS2CPacket {
    private final List<String> stages;
    private final AOperation operation;

    public ServerStagesSyncerS2CPacket(List<String> stages, AOperation operation) {
        this.stages = stages;
        this.operation = operation;
    }

    public ServerStagesSyncerS2CPacket(FriendlyByteBuf buf) {
        stages = buf.readList(FriendlyByteBuf::readUtf);
        operation = buf.readEnum(AOperation.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
        buf.writeEnum(operation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            switch (operation) {
                case ADD, ADD_ALL -> AStagesClientUtils.addStages(AClientHolder.server(), stages);
                case REMOVE, REMOVE_ALL -> AStagesClientUtils.removeStages(AClientHolder.server(), stages);
                case LOGIN -> AStagesClientUtils.setStages(AClientHolder.server(), stages);
            }

            MinecraftForge.EVENT_BUS.post(new ClientSynchronizeServerStagesEvent(stages, operation));
        });

        ctx.get().setPacketHandled(true);
    }
}
