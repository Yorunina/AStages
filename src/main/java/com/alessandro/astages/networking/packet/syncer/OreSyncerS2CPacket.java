package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.client.AClientOreRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import com.alessandro.astages.util.AStagesPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record OreSyncerS2CPacket(String id, String stage,
                                BlockState original,
                                BlockState replacement,
                                boolean requestReload) implements AStagesPacket {
    public static final Type<OreSyncerS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "ore_syncer_s2c_packet"));

    public static final StreamCodec<ByteBuf, OreSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, OreSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, OreSyncerS2CPacket::stage,
        ByteBufCodecs.fromCodec(BlockState.CODEC), OreSyncerS2CPacket::original,
        ByteBufCodecs.fromCodec(BlockState.CODEC), OreSyncerS2CPacket::replacement,
        ByteBufCodecs.BOOL, OreSyncerS2CPacket::requestReload,
        OreSyncerS2CPacket::new
    );

    @Override
    public void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> {
            var restriction = new AClientOreRestriction(id, stage, original, replacement);
            AClientRestrictionManager.ORE_INSTANCE.addRestriction(stage, restriction);

            if (requestReload) {
                NeoForge.EVENT_BUS.post(new ClientOreUpdateEvent());
            }
        }).exceptionally(e -> {
            AStages.LOGGER.debug(e.getLocalizedMessage());
            return null;
        });
    }

    @Contract(pure = true)
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
