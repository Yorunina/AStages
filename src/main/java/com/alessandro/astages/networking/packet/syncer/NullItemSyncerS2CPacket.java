package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.util.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record NullItemSyncerS2CPacket(ItemStack stack) implements AStagesPacket {
    public static final Type<NullItemSyncerS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "null_item_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NullItemSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC, NullItemSyncerS2CPacket::stack,
        NullItemSyncerS2CPacket::new
    );

    @Override
    public void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> AClientRestrictionManager.ITEM_INSTANCE.notRestricted(stack)).exceptionally(e -> {
            AStages.LOGGER.debug(e.getLocalizedMessage());
            return null;
        });
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
