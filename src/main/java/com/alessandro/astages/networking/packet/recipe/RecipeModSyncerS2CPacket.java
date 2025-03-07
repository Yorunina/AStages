package com.alessandro.astages.networking.packet.recipe;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.AClientRecipeModRestriction;
import com.alessandro.astages.core.restriction.ARecipeRestriction;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@Info("For now, required only by JEI.")
public record RecipeModSyncerS2CPacket(String id, String stage, String modId) implements AStagesPacket {
    public static final CustomPacketPayload.Type<RecipeModSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "recipe_mod_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeModSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, RecipeModSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, RecipeModSyncerS2CPacket::stage,
        ByteBufCodecs.STRING_UTF8, RecipeModSyncerS2CPacket::modId,
        RecipeModSyncerS2CPacket::new
    );

    public RecipeModSyncerS2CPacket(@NotNull ARecipeRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModId());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientRecipeModRestriction(id, stage, modId);
        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
