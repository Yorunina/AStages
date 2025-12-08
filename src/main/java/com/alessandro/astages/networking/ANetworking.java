package com.alessandro.astages.networking;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.networking.packet.dimension.DimensionIdsSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.*;
import com.alessandro.astages.networking.packet.mob.MobSyncerS2CPacket;
import com.alessandro.astages.networking.packet.ore.OreSyncerS2CPacket;
import com.alessandro.astages.networking.packet.recipe.RecipeModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.recipe.RecipeSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestRestrictionDeleteS2CPacket;
import com.alessandro.astages.networking.packet.simple.SimpleIdsSyncerS2CPacket;
import com.alessandro.astages.networking.packet.stages.ClientStagesSyncerS2CPacket;
import com.alessandro.astages.networking.packet.stages.ServerStagesSyncerS2CPacket;
import com.alessandro.astages.networking.packet.stages.StageDisplaySyncerS2CPacket;
import com.alessandro.astages.networking.packet.stages.StagesSyncerS2CPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;

@NotNullParams
public class ANetworking {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(AResourceLocation.fromNamespaceAndPath("messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        // STAGES
//        registerPacket(ClientStagesSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(ClientStagesSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientStagesSyncerS2CPacket::new)
                .encoder(ClientStagesSyncerS2CPacket::toBytes)
                .consumerMainThread(ClientStagesSyncerS2CPacket::handle)
                .add();

//        registerPacket(StagesSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(StagesSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(StagesSyncerS2CPacket::new)
            .encoder(StagesSyncerS2CPacket::toBytes)
            .consumerMainThread(StagesSyncerS2CPacket::handle)
            .add();

//        registerPacket(StageDisplaySyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(StageDisplaySyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(StageDisplaySyncerS2CPacket::new)
            .encoder(StageDisplaySyncerS2CPacket::toBytes)
            .consumerMainThread(StageDisplaySyncerS2CPacket::handle)
            .add();

        // ITEMS
//        registerPacket(ItemSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(ItemSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemSyncerS2CPacket::new)
            .encoder(ItemSyncerS2CPacket::toBytes)
            .consumerMainThread(ItemSyncerS2CPacket::handle)
            .add();

//        registerPacket(ItemTagSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(ItemTagSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemTagSyncerS2CPacket::new)
            .encoder(ItemTagSyncerS2CPacket::toBytes)
            .consumerMainThread(ItemTagSyncerS2CPacket::handle)
            .add();

//        registerPacket(ItemModSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(ItemModSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemModSyncerS2CPacket::new)
            .encoder(ItemModSyncerS2CPacket::toBytes)
            .consumerMainThread(ItemModSyncerS2CPacket::handle)
            .add();

//        registerPacket(ItemPredicateSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(ItemPredicateSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemPredicateSyncerS2CPacket::new)
            .encoder(ItemPredicateSyncerS2CPacket::toBytes)
            .consumerMainThread(ItemPredicateSyncerS2CPacket::handle)
            .add();

//        registerPacket(ItemPropertySyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(ItemPropertySyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ItemPropertySyncerS2CPacket::new)
            .encoder(ItemPropertySyncerS2CPacket::toBytes)
            .consumerMainThread(ItemPropertySyncerS2CPacket::handle)
            .add();

//        registerPacket(RequestItemPropertyC2SPacket.class, NetworkDirection.PLAY_TO_SERVER);
        INSTANCE.messageBuilder(RequestItemPropertyC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(RequestItemPropertyC2SPacket::new)
            .encoder(RequestItemPropertyC2SPacket::toBytes)
            .consumerMainThread(RequestItemPropertyC2SPacket::handle)
            .add();

        // RECIPES
//        registerPacket(RecipeSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(RecipeSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RecipeSyncerS2CPacket::new)
            .encoder(RecipeSyncerS2CPacket::toBytes)
            .consumerMainThread(RecipeSyncerS2CPacket::handle)
            .add();

//        registerPacket(RecipeModSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(RecipeModSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RecipeModSyncerS2CPacket::new)
            .encoder(RecipeModSyncerS2CPacket::toBytes)
            .consumerMainThread(RecipeModSyncerS2CPacket::handle)
            .add();

        // ORES
//        registerPacket(OreSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(OreSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(OreSyncerS2CPacket::new)
            .encoder(OreSyncerS2CPacket::toBytes)
            .consumerMainThread(OreSyncerS2CPacket::handle)
            .add();

        // MOB
//        registerPacket(MobSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(MobSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(MobSyncerS2CPacket::new)
            .encoder(MobSyncerS2CPacket::toBytes)
            .consumerMainThread(MobSyncerS2CPacket::handle)
            .add();

        // DIMENSION
//        registerPacket(DimensionIdsSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(DimensionIdsSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(DimensionIdsSyncerS2CPacket::new)
            .encoder(DimensionIdsSyncerS2CPacket::toBytes)
            .consumerMainThread(DimensionIdsSyncerS2CPacket::handle)
            .add();

        // SERVER
//        registerPacket(ServerStagesSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(ServerStagesSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ServerStagesSyncerS2CPacket::new)
            .encoder(ServerStagesSyncerS2CPacket::toBytes)
            .consumerMainThread(ServerStagesSyncerS2CPacket::handle)
            .add();

        // SIMPLE
//        registerPacket(SimpleIdsSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SimpleIdsSyncerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SimpleIdsSyncerS2CPacket::new)
                .encoder(SimpleIdsSyncerS2CPacket::toBytes)
                .consumerMainThread(SimpleIdsSyncerS2CPacket::handle)
                .add();

        // RELOADING
//        registerPacket(RequestReloadS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(RequestReloadS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RequestReloadS2CPacket::new)
                .encoder(RequestReloadS2CPacket::toBytes)
                .consumerMainThread(RequestReloadS2CPacket::handle)
                .add();

//        registerPacket(RequestRestrictionDeleteS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(RequestRestrictionDeleteS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RequestRestrictionDeleteS2CPacket::new)
                .encoder(RequestRestrictionDeleteS2CPacket::toBytes)
                .consumerMainThread(RequestRestrictionDeleteS2CPacket::handle)
                .add();
    }

    @Info("Send to server!")
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    @Info("Send to client!")
    public static <MSG> void sendToPlayer(ServerPlayer player, MSG message) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    @Info("Send to client!")
    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    @Info("Send to client!")
    public static <MSG> void sendTo(@Nullable ServerPlayer player, MSG message) {
        if (player == null) { // If Null -> Whole Server!
            ANetworking.sendToAllPlayers(message);
        } else {
            ANetworking.sendToPlayer(player, message);
        }
    }

    public static <T extends AStagesPacket> void registerPacket(Class<T> packetClass, NetworkDirection direction) {
        INSTANCE.messageBuilder(packetClass, id(), direction)
            .decoder(buf -> {
                try {
                    return packetClass.getConstructor(FriendlyByteBuf.class).newInstance(buf);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                         InvocationTargetException exception) {
                    throw new RuntimeException("Missing FriendlyByteBuf in " + packetClass + "!");
                }
            })
            .encoder(AStagesPacket::toBytes)
            .consumerMainThread(AStagesPacket::handle)
            .add();
    }
}
