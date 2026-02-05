package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.constant.AStageSource;
import com.alessandro.astages.api.develop.NotYetImplemented;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

@NotNullParams
public class ClientStagesC2SPacket implements AStagesPacket {
    private final AStageSource requester;
    private final AStageSource askedFor;
    private final UUID requesterUUID;
    private final UUID playerUUID;
    private final Set<String> stages;

    public ClientStagesC2SPacket(AStageSource requester, AStageSource askedFor, @Nullable UUID requesterUUID, @Nullable UUID playerUUID, Set<String> stages) {
        this.requester = requester;
        this.askedFor = askedFor;
        this.requesterUUID = requesterUUID;
        this.playerUUID = playerUUID;
        this.stages = stages;
    }
    
    public ClientStagesC2SPacket(FriendlyByteBuf buf) {
        requester = buf.readEnum(AStageSource.class);
        askedFor = buf.readEnum(AStageSource.class);
        requesterUUID = requester == AStageSource.PLAYER ? buf.readUUID() : null;
        playerUUID = askedFor == AStageSource.PLAYER ? buf.readUUID() : null;
        stages = buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(requester);
        buf.writeEnum(askedFor);
        if (requester == AStageSource.PLAYER) { buf.writeUUID(requesterUUID); }
        if (askedFor == AStageSource.PLAYER) { buf.writeUUID(playerUUID); }
        buf.writeCollection(stages, FriendlyByteBuf::writeUtf);
    }

    @NotYetImplemented("Prefer AChatBundle")
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        // HERE WE ARE ON SERVER!
        ctx.get().enqueueWork(() -> {
            var server = ServerLifecycleHooks.getCurrentServer();
            
            CommandSource executor = requester == AStageSource.SERVER ? server : server.getPlayerList().getPlayer(requesterUUID);
            var playerChecked = askedFor == AStageSource.PLAYER ? server.getPlayerList().getPlayer(playerUUID) : null;

            if (executor == null) { return; }

            if (askedFor == AStageSource.PLAYER) {
                if (playerChecked == null) { return; }

                if (stages.isEmpty()) {
                    executor.sendSystemMessage(Component.translatable("chat.astages.info.no_stages", playerChecked.getName()).withStyle(ChatFormatting.RED));
                } else {
                    executor.sendSystemMessage(Component.translatable("chat.astages.info.has_stages", playerChecked.getName()).withStyle(ChatFormatting.GREEN));
                    for (var stage : stages) {
                        executor.sendSystemMessage(Component.translatable("chat.astages.info.list_item", stage));
                    }
                }
            } else if (askedFor == AStageSource.SERVER) {
                if (stages.isEmpty()) {
                    executor.sendSystemMessage(Component.translatable("chat.astages.info.server.no_stages").withStyle(ChatFormatting.RED));
                } else {
                    executor.sendSystemMessage(Component.translatable("chat.astages.info.server.has_stages").withStyle(ChatFormatting.GREEN));
                    for (var stage : stages) {
                        executor.sendSystemMessage(Component.translatable("chat.astages.info.server.list_item", stage));
                    }
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
