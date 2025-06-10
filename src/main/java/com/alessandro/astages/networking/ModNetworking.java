package com.alessandro.astages.networking;

import com.alessandro.astages.AStages;
import com.alessandro.astages.networking.packet.reload.RequestRestrictionDeleteS2CPacket;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.alessandro.astages.networking.packet.dimension.DimensionIdsSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.*;
import com.alessandro.astages.networking.packet.mob.MobSyncerS2CPacket;
import com.alessandro.astages.networking.packet.ore.OreSyncerS2CPacket;
import com.alessandro.astages.networking.packet.recipe.RecipeModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.recipe.RecipeSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
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

        net.messageBuilder(ItemTagSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemTagSyncerS2CPacket::new)
            .encoder(ItemTagSyncerS2CPacket::toBytes)
            .consumerMainThread(ItemTagSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(ItemModSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemModSyncerS2CPacket::new)
            .encoder(ItemModSyncerS2CPacket::toBytes)
            .consumerMainThread(ItemModSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(ItemPredicateSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemPredicateSyncerS2CPacket::new)
            .encoder(ItemPredicateSyncerS2CPacket::toBytes)
            .consumerMainThread(ItemPredicateSyncerS2CPacket::handle)
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

        // ORES
        net.messageBuilder(OreSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(OreSyncerS2CPacket::new)
            .encoder(OreSyncerS2CPacket::toBytes)
            .consumerMainThread(OreSyncerS2CPacket::handle)
            .add();

        // MOB
        net.messageBuilder(MobSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(MobSyncerS2CPacket::new)
            .encoder(MobSyncerS2CPacket::toBytes)
            .consumerMainThread(MobSyncerS2CPacket::handle)
            .add();

        // DIMENSION
        net.messageBuilder(DimensionIdsSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(DimensionIdsSyncerS2CPacket::new)
            .encoder(DimensionIdsSyncerS2CPacket::toBytes)
            .consumerMainThread(DimensionIdsSyncerS2CPacket::handle)
            .add();

        // SERVER
        net.messageBuilder(ServerStagesSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ServerStagesSyncerS2CPacket::new)
            .encoder(ServerStagesSyncerS2CPacket::toBytes)
            .consumerMainThread(ServerStagesSyncerS2CPacket::handle)
            .add();

        // RELOADING
        net.messageBuilder(RequestReloadS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RequestReloadS2CPacket::new)
                .encoder(RequestReloadS2CPacket::toBytes)
                .consumerMainThread(RequestReloadS2CPacket::handle)
                .add();

        net.messageBuilder(RequestRestrictionDeleteS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RequestRestrictionDeleteS2CPacket::new)
                .encoder(RequestRestrictionDeleteS2CPacket::toBytes)
                .consumerMainThread(RequestRestrictionDeleteS2CPacket::handle)
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
