package com.alessandro.astages.networking.packet.dimension;

import com.alessandro.astages.core.AClientRestrictionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class DimensionIdsSyncerS2CPacket {
    private final List<String> ids;

    public DimensionIdsSyncerS2CPacket(List<String> ids) {
        this.ids = ids;
    }

    public DimensionIdsSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        ids = buf.readList(FriendlyByteBuf::readUtf);
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeCollection(ids, FriendlyByteBuf::writeUtf);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            AClientRestrictionManager.DIMENSION_IDS.clear();
            AClientRestrictionManager.DIMENSION_IDS.addAll(ids);
        });

        ctx.get().setPacketHandled(true);
    }
}
