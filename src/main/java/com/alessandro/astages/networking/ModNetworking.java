package com.alessandro.astages.networking;

import com.alessandro.astages.AStages;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.alessandro.astages.networking.packet.item.*;
import com.alessandro.astages.networking.packet.mob.MobSyncerS2CPacket;
import com.alessandro.astages.networking.packet.ore.OreStagesSyncerS2CPacket;
import com.alessandro.astages.networking.packet.ore.OreSyncerS2CPacket;
import com.alessandro.astages.networking.packet.recipe.RecipeModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.recipe.RecipeSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestClientReloadS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestItemReloadS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestRecipeReloadS2CPacket;
import com.alessandro.astages.networking.packet.server.ServerStagesSyncerS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;

public class ModNetworking {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(AStages.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // STAGES
        net.messageBuilder(StageDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StageDataSyncS2CPacket::new)
                .encoder(StageDataSyncS2CPacket::toBytes)
                .consumerMainThread(StageDataSyncS2CPacket::handle)
                .add();

        // ITEMS
        net.messageBuilder(ItemSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemSyncerS2CPacket::new)
            .encoder(ItemSyncerS2CPacket::toBytes)
            .consumerMainThread(ItemSyncerS2CPacket::handle)
            .add();

//        net.messageBuilder(ItemSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
//            .decoder(com.alessandro.astages.networking.packet.item.ItemSyncerS2CPacket::new)
//            .encoder(com.alessandro.astages.networking.packet.item.ItemSyncerS2CPacket::toBytes)
//            .consumerMainThread(com.alessandro.astages.networking.packet.item.ItemSyncerS2CPacket::handle)
//            .add();

        net.messageBuilder(TagSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(TagSyncerS2CPacket::new)
            .encoder(TagSyncerS2CPacket::toBytes)
            .consumerMainThread(TagSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(ModSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ModSyncerS2CPacket::new)
            .encoder(ModSyncerS2CPacket::toBytes)
            .consumerMainThread(ModSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(PredicateSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(PredicateSyncerS2CPacket::new)
            .encoder(PredicateSyncerS2CPacket::toBytes)
            .consumerMainThread(PredicateSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(ItemPropertySyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemPropertySyncerS2CPacket::new)
            .encoder(ItemPropertySyncerS2CPacket::toBytes)
            .consumerMainThread(ItemPropertySyncerS2CPacket::handle)
            .add();

        net.messageBuilder(RequestItemPropertyC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(RequestItemPropertyC2SPacket::new)
            .encoder(RequestItemPropertyC2SPacket::toBytes)
            .consumerMainThread(RequestItemPropertyC2SPacket::handle)
            .add();

        // JEI
        net.messageBuilder(RequestItemReloadS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RequestItemReloadS2CPacket::new)
            .encoder(RequestItemReloadS2CPacket::toBytes)
            .consumerMainThread(RequestItemReloadS2CPacket::handle)
            .add();

        // RECIPES
        net.messageBuilder(RecipeSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RecipeSyncerS2CPacket::new)
            .encoder(RecipeSyncerS2CPacket::toBytes)
            .consumerMainThread(RecipeSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(RecipeModSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RecipeModSyncerS2CPacket::new)
            .encoder(RecipeModSyncerS2CPacket::toBytes)
            .consumerMainThread(RecipeModSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(RequestRecipeReloadS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RequestRecipeReloadS2CPacket::new)
            .encoder(RequestRecipeReloadS2CPacket::toBytes)
            .consumerMainThread(RequestRecipeReloadS2CPacket::handle)
            .add();


        // ORES
        net.messageBuilder(OreSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(OreSyncerS2CPacket::new)
            .encoder(OreSyncerS2CPacket::toBytes)
            .consumerMainThread(OreSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(OreStagesSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(OreStagesSyncerS2CPacket::new)
            .encoder(OreStagesSyncerS2CPacket::toBytes)
            .consumerMainThread(OreStagesSyncerS2CPacket::handle)
            .add();

        // MOB
        net.messageBuilder(MobSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(MobSyncerS2CPacket::new)
            .encoder(MobSyncerS2CPacket::toBytes)
            .consumerMainThread(MobSyncerS2CPacket::handle)
            .add();

        // SERVER
        net.messageBuilder(ServerStagesSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ServerStagesSyncerS2CPacket::new)
            .encoder(ServerStagesSyncerS2CPacket::toBytes)
            .consumerMainThread(ServerStagesSyncerS2CPacket::handle)
            .add();

        // RELOADING
        net.messageBuilder(RequestClientReloadS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RequestClientReloadS2CPacket::new)
            .encoder(RequestClientReloadS2CPacket::toBytes)
            .consumerMainThread(RequestClientReloadS2CPacket::handle)
            .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendTo(@Nullable ServerPlayer player, MSG message) {
        if (player == null) { // If Null -> Whole Server!
            ModNetworking.sendToClients(message);
        } else {
            ModNetworking.sendToPlayer(message, player);
        }
    }
}
