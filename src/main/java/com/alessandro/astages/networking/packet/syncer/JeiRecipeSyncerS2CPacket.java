package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.core.client.AClientRecipeRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class JeiRecipeSyncerS2CPacket {
    private final String id;
    private final String stage;
    private final int priority;
    private final RecipeType<?> type;
    private final List<ResourceLocation> recipes;

    public JeiRecipeSyncerS2CPacket(String id, String stage, int priority, RecipeType<?> type, List<ResourceLocation> recipes) {
        this.id = id;
        this.stage = stage;
        this.priority = priority;
        this.type = type;
        this.recipes = recipes;
    }

    public JeiRecipeSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
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

            MinecraftForge.EVENT_BUS.post(new ClientRecipeUpdateEvent());
        });

        ctx.get().setPacketHandled(true);
    }
}
