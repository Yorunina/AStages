package com.alessandro.astages.networking.packet;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

@NotNullParamsAndMethodsReturn
public abstract class StageSyncerPacket {
    private final List<String> stages;
    private final AOperation operation;

    public StageSyncerPacket(List<String> stages, AOperation operation) {
        this.stages = stages;
        this.operation = operation;
    }

    public StageSyncerPacket(FriendlyByteBuf buf) {
        this.stages = buf.readList(FriendlyByteBuf::readUtf);
        this.operation = buf.readEnum(AOperation.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
        buf.writeEnum(operation);
    }

    public abstract void handle();

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(this::handle);
        ctx.get().setPacketHandled(true);
    }

    public List<String> getStages() {
        return stages;
    }

    public AOperation getOperation() {
        return operation;
    }
}
