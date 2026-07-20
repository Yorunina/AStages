package com.alessandro.astages.infrastructure.networking.packet.reload;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@NotNullParams
public class LogUnknowModelsS2C implements AStagesPacket {
    private final Set<ResourceLocation> unknownModels;

    public LogUnknowModelsS2C(Set<ResourceLocation> unknownModels) {
        this.unknownModels = unknownModels;
    }

    public LogUnknowModelsS2C(FriendlyByteBuf buf) {
        unknownModels = buf.readCollection(HashSet::new, FriendlyByteBuf::readResourceLocation);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(unknownModels, FriendlyByteBuf::writeResourceLocation);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            AStages.LOGGER.warn(Component.translatable("message.astages.missing_model.warning", unknownModels).getString())
        );

        ctx.get().setPacketHandled(true);
    }
}
