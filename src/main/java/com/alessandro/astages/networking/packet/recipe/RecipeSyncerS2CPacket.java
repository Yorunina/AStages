package com.alessandro.astages.networking.packet.recipe;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.AClientRecipeRestriction;
import com.alessandro.astages.core.restriction.ARecipeRestriction;
import com.alessandro.astages.networking.ACodes;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Info("For now, required only by JEI.")
public record RecipeSyncerS2CPacket(String id, String stage, int priority, RecipeType<?> recipeType, List<ResourceLocation> recipes) implements AStagesPacket {
    public static final CustomPacketPayload.Type<RecipeSyncerS2CPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AStages.MODID, "recipe_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, RecipeSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, RecipeSyncerS2CPacket::stage,
        ByteBufCodecs.INT, RecipeSyncerS2CPacket::priority,
        ByteBufCodecs.registry(Registries.RECIPE_TYPE), RecipeSyncerS2CPacket::recipeType,
        ACodes.RESOURCE_LOCATION.apply(ByteBufCodecs.list()), RecipeSyncerS2CPacket::recipes,
        RecipeSyncerS2CPacket::new
    );

    public RecipeSyncerS2CPacket(@NotNull ARecipeRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.getType(), restriction.getRecipes());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientRecipeRestriction(id, stage, priority, recipeType, recipes);
        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(stage, restriction);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
