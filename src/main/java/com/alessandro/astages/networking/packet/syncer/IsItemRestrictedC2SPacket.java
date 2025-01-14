package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record IsItemRestrictedC2SPacket(ItemStack stack) implements AStagesPacket {
    public static final CustomPacketPayload.Type<IsItemRestrictedC2SPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "is_item_restricted_c2s_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, IsItemRestrictedC2SPacket> STREAM_CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC,
        IsItemRestrictedC2SPacket::stack,
        IsItemRestrictedC2SPacket::new
    );

    @Override
    public void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> {
            // HERE WE ARE ON SERVER
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(stack); // Regardless to player...

            if (restriction != null) {
                PacketDistributor.sendToPlayer((ServerPlayer) context.player(), new ItemSyncerS2CPacket(restriction.getId(), restriction.getStage(), stack, restriction.getAttribute(Attributes.RENDERING_NAME), restriction.getAttribute(Attributes.HIDING_TOOLTIP), restriction.getMessage(Attributes.Item.HIDDEN_NAME, stack), restriction.getMessage(Attributes.Item.JADE_ITEM_MESSAGE, stack), restriction.getMessage(Attributes.Item.JADE_BLOCK_MESSAGE, stack)));
            } else {
                PacketDistributor.sendToPlayer((ServerPlayer) context.player(), new NullItemSyncerS2CPacket(stack));
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
