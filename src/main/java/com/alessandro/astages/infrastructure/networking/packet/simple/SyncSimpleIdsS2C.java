package com.alessandro.astages.infrastructure.networking.packet.simple;

import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.client.ClientMiscStorage;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

@NotNullParams
public class SyncSimpleIdsS2C implements AStagesPacket {
    private final Collection<String> ids;
    private final ASyncOperation operation;

    public SyncSimpleIdsS2C(Collection<String> ids, ASyncOperation operation) {
        this.ids = ids;
        this.operation = operation;
    }

    public SyncSimpleIdsS2C(FriendlyByteBuf buf) {
        ids = buf.readList(FriendlyByteBuf::readUtf);
        operation = buf.readEnum(ASyncOperation.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(ids, FriendlyByteBuf::writeUtf);
        buf.writeEnum(operation);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            switch (operation) {
                case ADD -> ClientMiscStorage.SIMPLE_IDS.addAll(ids);
                case REMOVE -> ids.forEach(ClientMiscStorage.SIMPLE_IDS::remove);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
