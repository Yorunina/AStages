package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.item.AClientItemPropertyRestriction;
import com.alessandro.astages.networking.ACodes;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ItemPropertySyncerS2CPacket(String id, String stage, ItemStack stack, boolean renderItemName, boolean hideTooltip, Component tooltipMessage, Component jadeItemMessage, Component jadeBlockMessage) implements AStagesPacket {
    public static final CustomPacketPayload.Type<ItemPropertySyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "item_property_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemPropertySyncerS2CPacket> STREAM_CODEC = ACodes.composite(
        ByteBufCodecs.STRING_UTF8, ItemPropertySyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, ItemPropertySyncerS2CPacket::stage,
        ItemStack.STREAM_CODEC, ItemPropertySyncerS2CPacket::stack,
        ByteBufCodecs.BOOL, ItemPropertySyncerS2CPacket::renderItemName,
        ByteBufCodecs.BOOL, ItemPropertySyncerS2CPacket::hideTooltip,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), ItemPropertySyncerS2CPacket::tooltipMessage,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), ItemPropertySyncerS2CPacket::jadeItemMessage,
        ByteBufCodecs.fromCodec(ComponentSerialization.CODEC), ItemPropertySyncerS2CPacket::jadeBlockMessage,
        ItemPropertySyncerS2CPacket::new
    );

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemPropertyRestriction(id, stage, stack, renderItemName, hideTooltip, tooltipMessage, jadeItemMessage, jadeBlockMessage);
        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
