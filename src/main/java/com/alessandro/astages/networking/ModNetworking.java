package com.alessandro.astages.networking;

import com.alessandro.astages.AStages;
import com.alessandro.astages.networking.packet.RequestReRenderingS2CPacket;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.alessandro.astages.networking.packet.syncer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

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

        net.messageBuilder(RequestReRenderingS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RequestReRenderingS2CPacket::new)
            .encoder(RequestReRenderingS2CPacket::toBytes)
            .consumerMainThread(RequestReRenderingS2CPacket::handle)
            .add();

        // ITEMS
        net.messageBuilder(IsItemRestrictedC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(IsItemRestrictedC2SPacket::new)
            .encoder(IsItemRestrictedC2SPacket::toBytes)
            .consumerMainThread(IsItemRestrictedC2SPacket::handle)
            .add();

        net.messageBuilder(ItemSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemSyncerS2CPacket::new)
            .encoder(ItemSyncerS2CPacket::toBytes)
            .consumerMainThread(ItemSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(NullItemSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(NullItemSyncerS2CPacket::new)
            .encoder(NullItemSyncerS2CPacket::toBytes)
            .consumerMainThread(NullItemSyncerS2CPacket::handle)
            .add();

        // JEI
        net.messageBuilder(IsJeiRestrictedC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(IsJeiRestrictedC2SPacket::new)
            .encoder(IsJeiRestrictedC2SPacket::toBytes)
            .consumerMainThread(IsJeiRestrictedC2SPacket::handle)
            .add();

        net.messageBuilder(JeiIsRestrictedS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(JeiIsRestrictedS2CPacket::new)
            .encoder(JeiIsRestrictedS2CPacket::toBytes)
            .consumerMainThread(JeiIsRestrictedS2CPacket::handle)
            .add();

        net.messageBuilder(JeiSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(JeiSyncerS2CPacket::new)
            .encoder(JeiSyncerS2CPacket::toBytes)
            .consumerMainThread(JeiSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(RequestJeiClientReloadS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RequestJeiClientReloadS2CPacket::new)
            .encoder(RequestJeiClientReloadS2CPacket::toBytes)
            .consumerMainThread(RequestJeiClientReloadS2CPacket::handle)
            .add();

        // RECIPES
        net.messageBuilder(JeiRecipeSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(JeiRecipeSyncerS2CPacket::new)
            .encoder(JeiRecipeSyncerS2CPacket::toBytes)
            .consumerMainThread(JeiRecipeSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(RequestJeiRecipeReloadS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RequestJeiRecipeReloadS2CPacket::new)
            .encoder(RequestJeiRecipeReloadS2CPacket::toBytes)
            .consumerMainThread(RequestJeiRecipeReloadS2CPacket::handle)
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
}
