package com.alessandro.astages.networking.packet.dimension;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

@NotNullParams
public class DimensionIdsSyncerS2CPacket {
    private final List<String> ids;

    public DimensionIdsSyncerS2CPacket(List<String> ids) {
        this.ids = ids;
    }

    public DimensionIdsSyncerS2CPacket(FriendlyByteBuf buf) {
        ids = buf.readList(FriendlyByteBuf::readUtf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(ids, FriendlyByteBuf::writeUtf);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            AClientRestrictionManager.DIMENSION_IDS.clear();
            AClientRestrictionManager.DIMENSION_IDS.addAll(ids);
        });

        ctx.get().setPacketHandled(true);
    }
}
