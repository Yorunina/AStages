package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AStageSource;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

@NotNullParams
public class RequestClientStagesS2CPacket implements AStagesPacket {
    private final AStageSource requester;
    private final AStageSource askedFor;
    private final UUID requesterUUID;
    private final UUID playerUUID;

    public RequestClientStagesS2CPacket(AStageSource requester, AStageSource askedFor, @Nullable UUID requesterUUID, @Nullable UUID playerUUID) {
        this.requester = requester;
        this.askedFor = askedFor;
        this.requesterUUID = requesterUUID;
        this.playerUUID = playerUUID;
    }

    public RequestClientStagesS2CPacket(FriendlyByteBuf buf) {
        requester = buf.readEnum(AStageSource.class);
        askedFor = buf.readEnum(AStageSource.class);
        requesterUUID = requester == AStageSource.PLAYER ? buf.readUUID() : null;
        playerUUID = askedFor == AStageSource.PLAYER ? buf.readUUID() : null;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(requester);
        buf.writeEnum(askedFor);
        if (requester == AStageSource.PLAYER) { buf.writeUUID(requesterUUID); }
        if (askedFor == AStageSource.PLAYER) { buf.writeUUID(playerUUID); }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        // HERE WE ARE ON CLIENT!
        ctx.get().enqueueWork(() -> {
            var stages = askedFor == AStageSource.PLAYER ?
                AStagesClientUtils.getStages(AClientHolder.player()) :
                AStagesClientUtils.getStages(AClientHolder.server());

            ANetworking.sendToServer(new ClientStagesC2SPacket(requester, askedFor, requesterUUID, playerUUID, stages));
        });

        ctx.get().setPacketHandled(true);
    }
}
