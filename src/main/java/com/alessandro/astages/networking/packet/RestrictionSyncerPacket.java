package com.alessandro.astages.networking.packet;

import com.alessandro.astages.util.annotations.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@NotNullParams
public abstract class RestrictionSyncerPacket {
    private final String id;
    private final String stage;

    public RestrictionSyncerPacket(String id, String stage) {
        this.id = id;
        this.stage = stage;
    }

    public RestrictionSyncerPacket(FriendlyByteBuf buf) {
        id = buf.readUtf();
        stage = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
    }

    public abstract void handle();

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(this::handle);
        ctx.get().setPacketHandled(true);
    }

    public String getId() {
        return id;
    }

    public String getStage() {
        return stage;
    }
}
