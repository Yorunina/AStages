package com.alessandro.astages.networking.packet.recipe;

import com.alessandro.astages.core.client.AClientRecipeRestriction;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.restriction.ARecipeRestriction;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

@Info("For now, required only by JEI.")
public class RecipeSyncerS2CPacket {
    private final String id;
    private final String stage;
    private final int priority;
    private final RecipeType<?> type;
    private final List<ResourceLocation> recipes;

    public RecipeSyncerS2CPacket(@NotNull ARecipeRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.getType(), restriction.getRecipes());
    }

    public RecipeSyncerS2CPacket(String id, String stage, int priority, RecipeType<?> type, List<ResourceLocation> recipes) {
        this.id = id;
        this.stage = stage;
        this.priority = priority;
        this.type = type;
        this.recipes = recipes;
    }

    public RecipeSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        id = buf.readUtf();
        stage = buf.readUtf();
        priority = buf.readInt();
        type = buf.readRegistryIdUnsafe(ForgeRegistries.RECIPE_TYPES);
        recipes = buf.readList(FriendlyByteBuf::readResourceLocation);
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
        buf.writeInt(priority);
        buf.writeRegistryIdUnsafe(ForgeRegistries.RECIPE_TYPES, type);
        buf.writeCollection(recipes, FriendlyByteBuf::writeResourceLocation);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var restriction = new AClientRecipeRestriction(id, stage, priority, type, recipes);
            AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(stage, restriction);
        });

        ctx.get().setPacketHandled(true);
    }
}
