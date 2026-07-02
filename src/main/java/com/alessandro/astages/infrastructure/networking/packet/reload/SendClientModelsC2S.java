package com.alessandro.astages.infrastructure.networking.packet.reload;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientModelManager;
import com.alessandro.astages.engine.AModelManager;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.infrastructure.networking.Networking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@NotNullParams
public class SendClientModelsC2S implements AStagesPacket {
    private final Set<ResourceLocation> clientModels;

    public SendClientModelsC2S() {
        this.clientModels = AClientModelManager.MODELS.getModels();
    }

    public SendClientModelsC2S(Set<ResourceLocation> clientModels) {
        this.clientModels = clientModels;
    }

    public SendClientModelsC2S(FriendlyByteBuf buf) {
        clientModels = buf.readCollection(HashSet::new, FriendlyByteBuf::readResourceLocation);
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(clientModels, FriendlyByteBuf::writeResourceLocation);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = ctx.get().getSender();
            if (player == null) { return; }

            var serverModels = AModelManager.MODELS.getModels();

            // TODO: try replacing with Sets.difference()
            var missingModels = new HashSet<>(serverModels);
            missingModels.removeAll(clientModels);

            var unknownModels = new HashSet<>(clientModels);
            unknownModels.removeAll(serverModels);

            if (!unknownModels.isEmpty()) {
                Networking.sendTo(player, new LogUnknowModelsS2C(unknownModels));
            }

            if (!missingModels.isEmpty()) {
                var reason = Component.translatable("message.astages.missing_model.kick", missingModels).withStyle(ChatFormatting.RED);

                if (!unknownModels.isEmpty()) {
                    reason.append(Component.literal("\n\n"));
                    reason.append(
                        Component.translatable("message.astages.missing_model.warning", unknownModels).withStyle(ChatFormatting.GOLD)
                    );
                }

                player.connection.disconnect(reason);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
