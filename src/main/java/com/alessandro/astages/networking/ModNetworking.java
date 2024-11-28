package com.alessandro.astages.networking;

import com.alessandro.astages.AStages;
import com.alessandro.astages.networking.packet.ItemClientDataS2CPacket;
import com.alessandro.astages.networking.packet.RenderAtLoginS2CPacket;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.alessandro.astages.networking.packet.ud.*;
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

        net.messageBuilder(StageDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StageDataSyncS2CPacket::new)
                .encoder(StageDataSyncS2CPacket::toBytes)
                .consumerMainThread(StageDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(RenderAtLoginS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RenderAtLoginS2CPacket::new)
            .encoder(RenderAtLoginS2CPacket::toBytes)
            .consumerMainThread(RenderAtLoginS2CPacket::handle)
            .add();

        // UNDER DEVELOPMENT
//        net.messageBuilder(ItemClientDataS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
//            .decoder(ItemClientDataS2CPacket::new)
//            .encoder(ItemClientDataS2CPacket::toBytes)
//            .consumerMainThread(ItemClientDataS2CPacket::handle)
//            .add();

        net.messageBuilder(IsItemRestrictedC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(IsItemRestrictedC2SPacket::new)
            .encoder(IsItemRestrictedC2SPacket::toBytes)
            .consumerMainThread(IsItemRestrictedC2SPacket::handle)
            .add();
//
        net.messageBuilder(ItemIsRestrictedS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemIsRestrictedS2CPacket::new)
            .encoder(ItemIsRestrictedS2CPacket::toBytes)
            .consumerMainThread(ItemIsRestrictedS2CPacket::handle)
            .add();

//        net.messageBuilder(ItemIsNotRestrictedS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
//            .decoder(ItemIsNotRestrictedS2CPacket::new)
//            .encoder(ItemIsNotRestrictedS2CPacket::toBytes)
//            .consumerMainThread(ItemIsNotRestrictedS2CPacket::handle)
//            .add();

        net.messageBuilder(RecipeSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RecipeSyncerS2CPacket::new)
            .encoder(RecipeSyncerS2CPacket::toBytes)
            .consumerMainThread(RecipeSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(OreSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(OreSyncerS2CPacket::new)
            .encoder(OreSyncerS2CPacket::toBytes)
            .consumerMainThread(OreSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(RequestClientReloadS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RequestClientReloadS2CPacket::new)
            .encoder(RequestClientReloadS2CPacket::toBytes)
            .consumerMainThread(RequestClientReloadS2CPacket::handle)
            .add();

        net.messageBuilder(JeiSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(JeiSyncerS2CPacket::new)
            .encoder(JeiSyncerS2CPacket::toBytes)
            .consumerMainThread(JeiSyncerS2CPacket::handle)
            .add();

//        net.messageBuilder(SetTitleS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
//            .decoder(SetTitleS2CPacket::new)
//            .encoder(SetTitleS2CPacket::toBytes)
//            .consumerMainThread(SetTitleS2CPacket::handle)
//            .add();

        net.messageBuilder(ItemSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemSyncerS2CPacket::new)
            .encoder(ItemSyncerS2CPacket::toBytes)
            .consumerMainThread(ItemSyncerS2CPacket::handle)
            .add();

        net.messageBuilder(IsJeiRestrictedC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(IsJeiRestrictedC2SPacket::new)
            .encoder(IsJeiRestrictedC2SPacket::toBytes)
            .consumerMainThread(IsJeiRestrictedC2SPacket::handle)
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
