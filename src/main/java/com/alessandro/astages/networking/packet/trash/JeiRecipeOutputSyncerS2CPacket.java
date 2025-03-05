package com.alessandro.astages.networking.packet.trash;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class JeiRecipeOutputSyncerS2CPacket {
    private final String id;
    private final String stage;
    private final RecipeType<?> type;
    private final Item output;

    public JeiRecipeOutputSyncerS2CPacket(String id, String stage, RecipeType<?> type, Item output) {
        this.id = id;
        this.stage = stage;
        this.type = type;
        this.output = output;
    }

    public JeiRecipeOutputSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        id = buf.readUtf();
        stage = buf.readUtf();
        type = buf.readRegistryIdUnsafe(ForgeRegistries.RECIPE_TYPES);
        output = buf.readRegistryIdUnsafe(ForgeRegistries.ITEMS);
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
        buf.writeRegistryIdUnsafe(ForgeRegistries.RECIPE_TYPES, type);
        buf.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, output);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

        });

        ctx.get().setPacketHandled(true);
    }
}
