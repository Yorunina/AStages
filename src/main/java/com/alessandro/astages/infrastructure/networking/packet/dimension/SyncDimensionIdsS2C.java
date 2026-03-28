package com.alessandro.astages.infrastructure.networking.packet.dimension;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.client.ClientMiscStorage;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

@NotNullParams
public class SyncDimensionIdsS2C implements AStagesPacket {
    private final Collection<String> ids;

    public SyncDimensionIdsS2C(Collection<String> ids) {
        this.ids = ids;
    }

    public SyncDimensionIdsS2C(FriendlyByteBuf buf) {
        ids = buf.readList(FriendlyByteBuf::readUtf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(ids, FriendlyByteBuf::writeUtf);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            ClientMiscStorage.DIMENSION_IDS.clear();
            ClientMiscStorage.DIMENSION_IDS.addAll(ids);
        });

        ctx.get().setPacketHandled(true);
    }
}
