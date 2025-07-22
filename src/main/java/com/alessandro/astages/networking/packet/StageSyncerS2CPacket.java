package com.alessandro.astages.networking.packet;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.util.SyncOperation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class StageSyncerS2CPacket {
    private final Collection<String> stages;
    private final SyncOperation operation;

    public StageSyncerS2CPacket(Collection<String> stages, SyncOperation operation) {
        this.stages = stages;
        this.operation = operation;
    }

    public StageSyncerS2CPacket(FriendlyByteBuf buf) {
        stages = buf.readList(FriendlyByteBuf::readUtf);
        operation = buf.readEnum(SyncOperation.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
        buf.writeEnum(operation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (operation) {
                case ADD -> AClientRestrictionManager.ALL_STAGES.addAll(stages);
                case REMOVE -> AClientRestrictionManager.ALL_STAGES.removeAll(stages);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
