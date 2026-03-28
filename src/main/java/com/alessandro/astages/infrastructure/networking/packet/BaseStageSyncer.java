package com.alessandro.astages.infrastructure.networking.packet;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@NotNullParamsAndMethodsReturn
public abstract class BaseStageSyncer implements AStagesPacket {
    private final Set<String> stages;
    private final AOperation operation;

    public BaseStageSyncer(Set<String> stages, AOperation operation) {
        this.stages = stages;
        this.operation = operation;
    }

    public BaseStageSyncer(FriendlyByteBuf buf) {
        this.stages = buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf);
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

    public Set<String> getStages() {
        return stages;
    }

    public AOperation getOperation() {
        return operation;
    }
}
