package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.item.AClientModRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemModRestriction;
import com.alessandro.astages.networking.ACodes;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.store.Attributes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ModSyncerS2CPacket(String id, String stage, String modId, List<Item> ignoredItems, List<ResourceLocation> ignoredTags, boolean hideInJei) implements AStagesPacket {
    public static final CustomPacketPayload.Type<ModSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "mod_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ModSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, ModSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, ModSyncerS2CPacket::stage,
        ByteBufCodecs.STRING_UTF8, ModSyncerS2CPacket::modId,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()), ModSyncerS2CPacket::ignoredItems,
        ACodes.RESOURCE_LOCATION.apply(ByteBufCodecs.list()), ModSyncerS2CPacket::ignoredTags,
        ByteBufCodecs.BOOL, ModSyncerS2CPacket::hideInJei,
        ModSyncerS2CPacket::new
    );

    public ModSyncerS2CPacket(AItemModRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModId(), restriction.getIgnoredItems(), restriction.getIgnoredTags(), restriction.get(Attributes.HIDING_JEI));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientModRestriction(id(), stage()).setHideInJei(hideInJei);
        restriction.restrict(modId);
        restriction.ignoreItems(ignoredItems);
        restriction.ignoreTags(ignoredTags);
        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
