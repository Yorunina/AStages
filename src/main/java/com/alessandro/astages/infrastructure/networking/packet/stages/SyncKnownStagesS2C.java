package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.constant.AStageSource;
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
    private final AStageSource source;

    public SyncKnownStagesS2C(Collection<String> stages, ASyncOperation operation, AStageSource source) {
        this.stages = stages;
        this.operation = operation;
        this.source = source;
    }

    public SyncKnownStagesS2C(FriendlyByteBuf buf) {
        stages = buf.readList(FriendlyByteBuf::readUtf);
        operation = buf.readEnum(ASyncOperation.class);
        source = buf.readEnum(AStageSource.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
        buf.writeEnum(operation);
        buf.writeEnum(source);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (operation) {
                case ADD -> {
                    if (source == AStageSource.PLAYER) { ClientMiscStorage.STAGES_ONLY_FOR_PLAYER.addAll(stages); }
                    if (source == AStageSource.SERVER) { ClientMiscStorage.STAGES_ONLY_FOR_SERVER.addAll(stages); }
                    if (source == AStageSource.BOTH) { ClientMiscStorage.ALL_STAGES.addAll(stages); }
                }
                case REMOVE -> {
                    if (source == AStageSource.PLAYER) { ClientMiscStorage.STAGES_ONLY_FOR_PLAYER.removeAll(stages); }
                    if (source == AStageSource.SERVER) { ClientMiscStorage.STAGES_ONLY_FOR_SERVER.removeAll(stages); }
                    if (source == AStageSource.BOTH) { ClientMiscStorage.ALL_STAGES.removeAll(stages); }
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
