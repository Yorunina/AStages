package com.alessandro.astages.networking.packet.simple;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.util.SyncOperation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class SimpleStagesSyncerS2CPacket {
    private final Collection<String> ids;
    private final SyncOperation operation;

    public SimpleStagesSyncerS2CPacket(Collection<String> ids, SyncOperation operation) {
        this.ids = ids;
        this.operation = operation;
    }

    public SimpleStagesSyncerS2CPacket(FriendlyByteBuf buf) {
        ids = buf.readList(FriendlyByteBuf::readUtf);
        operation = buf.readEnum(SyncOperation.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(ids, FriendlyByteBuf::writeUtf);
        buf.writeEnum(operation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (operation) {
                case ADD -> AClientRestrictionManager.SIMPLE_IDS.addAll(ids);
                case REMOVE -> ids.forEach(AClientRestrictionManager.SIMPLE_IDS::remove);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
