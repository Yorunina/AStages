package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.client.ClientMiscStorage;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

@NotNullParams
public class SyncKnownStagesS2C implements AStagesPacket {
    private final Collection<String> stages;
    private final ASyncOperation operation;

    public SyncKnownStagesS2C(Collection<String> stages, ASyncOperation operation) {
        this.stages = stages;
        this.operation = operation;
    }

    public SyncKnownStagesS2C(FriendlyByteBuf buf) {
        stages = buf.readList(FriendlyByteBuf::readUtf);
        operation = buf.readEnum(ASyncOperation.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
        buf.writeEnum(operation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (operation) {
                case ADD -> ClientMiscStorage.ALL_STAGES.addAll(stages);
                case REMOVE -> ClientMiscStorage.ALL_STAGES.removeAll(stages);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
