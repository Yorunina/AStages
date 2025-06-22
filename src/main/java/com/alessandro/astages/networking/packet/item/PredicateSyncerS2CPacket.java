package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.item.AClientPredicateRestriction;
import com.alessandro.astages.core.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.networking.ACodes;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.store.Attributes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record PredicateSyncerS2CPacket(String id, String stage, ResourceLocation modelId, boolean hideInJei) implements AStagesPacket {
    public static final CustomPacketPayload.Type<PredicateSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "predicate_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PredicateSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, PredicateSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, PredicateSyncerS2CPacket::stage,
        ACodes.RESOURCE_LOCATION, PredicateSyncerS2CPacket::modelId,
        ByteBufCodecs.BOOL, PredicateSyncerS2CPacket::hideInJei,
        PredicateSyncerS2CPacket::new
    );

    public PredicateSyncerS2CPacket(AItemPredicateRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModelId(), restriction.get(Attributes.HIDING_JEI));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientPredicateRestriction(id(), stage()).setHideInJei(hideInJei);
        restriction.restrict(modelId);
        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
