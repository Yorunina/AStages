package com.alessandro.astages.networking.packet.ud;

import com.alessandro.astages.core.client.AClientRestrictionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class OreStagesSyncerS2CPacket {
    private final List<String> stages;

    public OreStagesSyncerS2CPacket(List<String> stages) {
        this.stages = stages;
    }

    public OreStagesSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        stages = buf.readList(FriendlyByteBuf::readUtf);
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            AClientRestrictionManager.ORE_STAGES.clear();
            AClientRestrictionManager.ORE_STAGES.addAll(stages);
        });

        ctx.get().setPacketHandled(true);
    }
}
