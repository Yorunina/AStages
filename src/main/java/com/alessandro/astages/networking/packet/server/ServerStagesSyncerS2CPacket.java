package com.alessandro.astages.networking.packet.server;

import com.alessandro.astages.core.AClientRestrictionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class ServerStagesSyncerS2CPacket {
    private final List<String> stages;

    public ServerStagesSyncerS2CPacket(List<String> stages) {
        this.stages = stages;
    }

    public ServerStagesSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        stages = buf.readList(FriendlyByteBuf::readUtf);
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            AClientRestrictionManager.SERVER_STAGES.clear();
            AClientRestrictionManager.SERVER_STAGES.addAll(stages);


        });

        ctx.get().setPacketHandled(true);
    }
}
