package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.client.AClientRecipeRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.alessandro.astages.util.ACodes;
import com.alessandro.astages.util.AStagesPacket;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record JeiRecipeSyncerS2CPacket(String id, String stage, Holder<RecipeType<?>> recipeType, List<ResourceLocation> recipes) implements AStagesPacket {
    public static final CustomPacketPayload.Type<JeiRecipeSyncerS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "jei_recipe_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, JeiRecipeSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, JeiRecipeSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, JeiRecipeSyncerS2CPacket::stage,
        ByteBufCodecs.holderRegistry(Registries.RECIPE_TYPE), JeiRecipeSyncerS2CPacket::recipeType,
        ACodes.RESOURCE_LOCATION.apply(ByteBufCodecs.list()), JeiRecipeSyncerS2CPacket::recipes,
        JeiRecipeSyncerS2CPacket::new
    );

    @Override
    public void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> {
            var restriction = new AClientRecipeRestriction(id, stage, recipeType.value(), recipes);
            AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(stage, restriction);

            NeoForge.EVENT_BUS.post(new ClientRecipeUpdateEvent());
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
