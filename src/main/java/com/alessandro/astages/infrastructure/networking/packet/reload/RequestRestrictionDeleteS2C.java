package com.alessandro.astages.infrastructure.networking.packet.reload;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.infrastructure.registry.AStagesRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@NotNullParams
public class RequestRestrictionDeleteS2C implements AStagesPacket {
    private final String id;
    private final ARestrictionType type;

    public RequestRestrictionDeleteS2C(String id, ARestrictionType type) {
        this.id = id;
        this.type = type;
    }

    public RequestRestrictionDeleteS2C(FriendlyByteBuf buf) {
        id = buf.readUtf();
        type = buf.readRegistryId();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeRegistryId(AStagesRegistries.RESTRICTION_TYPES, type);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> AClientRestrictionManager.removeRestriction(id, type));

        ctx.get().setPacketHandled(true);
    }
}
