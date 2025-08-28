package com.alessandro.astages.networking.packet;

import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

@NotNullParams
public class ClientStagesSyncerS2CPacket {
    private final List<String> stages;
    private final AOperation operation;

    public ClientStagesSyncerS2CPacket(List<String> stages, AOperation operation) {
        this.stages = stages;
        this.operation = operation;
    }

    public ClientStagesSyncerS2CPacket(FriendlyByteBuf buf) {
        this.stages = buf.readList(FriendlyByteBuf::readUtf);
        this.operation = buf.readEnum(AOperation.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
        buf.writeEnum(operation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            switch (operation) {
                case ADD -> AStagesClientUtils.addStage(AClientHolder.player(), stages.get(0));
                case ADD_ALL -> AStagesClientUtils.addStages(AClientHolder.player(), stages);
                case REMOVE -> AStagesClientUtils.removeStage(AClientHolder.player(), stages.get(0));
                case REMOVE_ALL -> AStagesClientUtils.removeStages(AClientHolder.player(), stages);
                case LOGIN -> AStagesClientUtils.setStages(AClientHolder.player(), stages);
            }

            MinecraftForge.EVENT_BUS.post(new ClientSynchronizeStagesEvent(stages, operation));
        });

        ctx.get().setPacketHandled(true);
    }
}
