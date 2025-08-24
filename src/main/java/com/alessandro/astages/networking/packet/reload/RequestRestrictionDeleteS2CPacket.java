package com.alessandro.astages.networking.packet.reload;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@NotNullParams
public class RequestRestrictionDeleteS2CPacket {
    private final String id;
    private final ARestrictionType type;

    public RequestRestrictionDeleteS2CPacket(String id, ARestrictionType type) {
        this.id = id;
        this.type = type;
    }

    public RequestRestrictionDeleteS2CPacket(FriendlyByteBuf buf) {
        id = buf.readUtf();
        type = buf.readEnum(ARestrictionType.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeEnum(type);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> AClientRestrictionManager.removeRestriction(id, type));

        ctx.get().setPacketHandled(true);
    }
}
