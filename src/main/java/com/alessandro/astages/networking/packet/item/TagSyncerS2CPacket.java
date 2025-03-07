package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.item.AClientTagRestriction;
import com.alessandro.astages.core.restriction.item.AItemTagRestriction;
import com.alessandro.astages.networking.ACodes;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.store.Attributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public record TagSyncerS2CPacket(String id, String stage, ResourceLocation tag, List<Item> ignoredItems, boolean hideInJei) implements AStagesPacket {
    public static final CustomPacketPayload.Type<TagSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "tag_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TagSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, TagSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, TagSyncerS2CPacket::stage,
        ACodes.RESOURCE_LOCATION, TagSyncerS2CPacket::tag,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()), TagSyncerS2CPacket::ignoredItems,
        ByteBufCodecs.BOOL, TagSyncerS2CPacket::hideInJei,
        TagSyncerS2CPacket::new
    );

    public TagSyncerS2CPacket(AItemTagRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getTag(), restriction.getIgnoredItems(), restriction.get(Attributes.HIDING_JEI));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientTagRestriction(id(), stage()).setHideInJei(hideInJei);
        restriction.restrict(tag);
        restriction.ignoreItems(ignoredItems);
        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
