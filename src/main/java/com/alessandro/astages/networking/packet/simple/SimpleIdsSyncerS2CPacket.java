package com.alessandro.astages.networking.packet.simple;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.function.Supplier;

@NotNullParams
public class SimpleIdsSyncerS2CPacket {
    private final Collection<String> ids;
    private final ASyncOperation operation;

    public SimpleIdsSyncerS2CPacket(Collection<String> ids, ASyncOperation operation) {
        this.ids = ids;
        this.operation = operation;
    }

    public SimpleIdsSyncerS2CPacket(FriendlyByteBuf buf) {
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
                case ADD -> AClientRestrictionManager.SIMPLE_IDS.addAll(ids);
                case REMOVE -> ids.forEach(AClientRestrictionManager.SIMPLE_IDS::remove);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
