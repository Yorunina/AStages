package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record IsJeiRestrictedC2SPacket(ItemStack stack, boolean requestReload) implements AStagesPacket {
    public static final CustomPacketPayload.Type<IsJeiRestrictedC2SPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "is_jei_restricted_c2s_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, IsJeiRestrictedC2SPacket> STREAM_CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC,
        IsJeiRestrictedC2SPacket::stack,
        ByteBufCodecs.BOOL,
        IsJeiRestrictedC2SPacket::requestReload,
        IsJeiRestrictedC2SPacket::new
    );

    @Override
    public void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> {
            // HERE WE ARE ON SERVER!
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(Objects.requireNonNull(context.player()), stack);

            if (restriction != null && restriction.isDisabled(Attributes.HIDING_JEI)) {
                // ModNetworking.sendToPlayer(new ItemIsRestrictedS2CPacket(stack, requestReload), ctx.get().getSender());
                PacketDistributor.sendToPlayer((ServerPlayer) context.player(), new JeiIsRestrictedS2CPacket(stack));
            }

            if (requestReload) {
                PacketDistributor.sendToPlayer((ServerPlayer) context.player(), new JeiSyncerS2CPacket());
            }
        }).exceptionally(e -> {
            AStages.LOGGER.debug(e.getLocalizedMessage());
            return null;
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
