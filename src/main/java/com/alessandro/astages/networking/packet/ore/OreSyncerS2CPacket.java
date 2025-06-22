package com.alessandro.astages.networking.packet.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.AClientOreRestriction;
import com.alessandro.astages.core.restriction.AOreRestriction;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record OreSyncerS2CPacket(String id, String stage, BlockState original, BlockState replacement) implements AStagesPacket {
    public static final CustomPacketPayload.Type<OreSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "ore_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OreSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, OreSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, OreSyncerS2CPacket::stage,
        ByteBufCodecs.fromCodec(BlockState.CODEC), OreSyncerS2CPacket::original,
        ByteBufCodecs.fromCodec(BlockState.CODEC), OreSyncerS2CPacket::replacement,
        OreSyncerS2CPacket::new
    );

    public OreSyncerS2CPacket(AOreRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getOriginal(), restriction.getReplacement());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientOreRestriction(id, stage, original, replacement);
        AClientRestrictionManager.ORE_INSTANCE.addRestriction(stage, restriction);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
