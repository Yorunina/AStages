package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.client.AClientItemRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.util.ACodes;
import com.alessandro.astages.util.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

// PROPERTIES MISSING
public record ItemSyncerS2CPacket(String id, String stage,
                                  ItemStack stack,
                                  boolean renderItemName,
                                  boolean hideTooltip,
                                  Component tooltipMessage,
                                  Component jadeItemMessage,
                                  Component jadeBlockMessage) implements AStagesPacket {
    public static final CustomPacketPayload.Type<ItemSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "item_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemSyncerS2CPacket> STREAM_CODEC = ACodes.composite(
        ByteBufCodecs.STRING_UTF8, ItemSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, ItemSyncerS2CPacket::stage,
        ItemStack.STREAM_CODEC, ItemSyncerS2CPacket::stack,
        ByteBufCodecs.BOOL, ItemSyncerS2CPacket::renderItemName,
        ByteBufCodecs.BOOL, ItemSyncerS2CPacket::hideTooltip,
        ACodes.COMPONENT, ItemSyncerS2CPacket::tooltipMessage,
        ACodes.COMPONENT, ItemSyncerS2CPacket::jadeItemMessage,
        ACodes.COMPONENT, ItemSyncerS2CPacket::jadeBlockMessage,
        ItemSyncerS2CPacket::new
    );

    @Override
    public void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            var restriction = new AClientItemRestriction(id, stage, stack, renderItemName, hideTooltip, tooltipMessage, jadeItemMessage, jadeBlockMessage);

            AClientRestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);
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
