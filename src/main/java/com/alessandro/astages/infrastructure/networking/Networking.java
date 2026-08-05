package com.alessandro.astages.infrastructure.networking;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.infrastructure.networking.packet.dimension.SyncDimensionIdsS2C;
import com.alessandro.astages.infrastructure.networking.packet.item.*;
import com.alessandro.astages.infrastructure.networking.packet.mob.SyncMobS2C;
import com.alessandro.astages.infrastructure.networking.packet.ore.SyncOreS2C;
import com.alessandro.astages.infrastructure.networking.packet.recipe.SyncRecipeModS2C;
import com.alessandro.astages.infrastructure.networking.packet.recipe.SyncRecipeS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.LogUnknowModelsS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestReloadS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestRestrictionDeleteS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.SendClientModelsC2S;
import com.alessandro.astages.infrastructure.networking.packet.simple.SyncSimpleIdsS2C;
import com.alessandro.astages.infrastructure.networking.packet.stages.*;
import com.alessandro.astages.infrastructure.networking.packet.structure.SyncRestrictedStructuresS2C;
import com.alessandro.astages.infrastructure.networking.packet.structure.SyncStructureS2C;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;

@NotNullParams
public class Networking {
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
        INSTANCE.messageBuilder(SyncPlayerStagesS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncPlayerStagesS2C::new)
                .encoder(SyncPlayerStagesS2C::toBytes)
                .consumerMainThread(SyncPlayerStagesS2C::handle)
                .add();

//        registerPacket(StagesSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncKnownStagesS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncKnownStagesS2C::new)
            .encoder(SyncKnownStagesS2C::toBytes)
            .consumerMainThread(SyncKnownStagesS2C::handle)
            .add();

//        registerPacket(StageDisplaySyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncPermanentStageS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncPermanentStageS2C::new)
            .encoder(SyncPermanentStageS2C::toBytes)
            .consumerMainThread(SyncPermanentStageS2C::handle)
            .add();

        INSTANCE.messageBuilder(SyncTemporaryStageS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncTemporaryStageS2C::new)
            .encoder(SyncTemporaryStageS2C::toBytes)
            .consumerMainThread(SyncTemporaryStageS2C::handle)
            .add();

        INSTANCE.messageBuilder(RequestClientStagesS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(RequestClientStagesS2C::new)
            .encoder(RequestClientStagesS2C::toBytes)
            .consumerMainThread(RequestClientStagesS2C::handle)
            .add();

        INSTANCE.messageBuilder(ReplyClientStagesC2S.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(ReplyClientStagesC2S::new)
            .encoder(ReplyClientStagesC2S::toBytes)
            .consumerMainThread(ReplyClientStagesC2S::handle)
            .add();

        // ITEMS
//        registerPacket(ItemSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncItemS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncItemS2C::new)
            .encoder(SyncItemS2C::toBytes)
            .consumerMainThread(SyncItemS2C::handle)
            .add();

//        registerPacket(ItemTagSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncItemTagS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncItemTagS2C::new)
            .encoder(SyncItemTagS2C::toBytes)
            .consumerMainThread(SyncItemTagS2C::handle)
            .add();

//        registerPacket(ItemModSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncItemModS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncItemModS2C::new)
            .encoder(SyncItemModS2C::toBytes)
            .consumerMainThread(SyncItemModS2C::handle)
            .add();

//        registerPacket(ItemPredicateSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncItemPredicateS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncItemPredicateS2C::new)
            .encoder(SyncItemPredicateS2C::toBytes)
            .consumerMainThread(SyncItemPredicateS2C::handle)
            .add();

//        registerPacket(ItemPropertySyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(ReplyItemPropertyS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ReplyItemPropertyS2C::new)
            .encoder(ReplyItemPropertyS2C::toBytes)
            .consumerMainThread(ReplyItemPropertyS2C::handle)
            .add();

//        registerPacket(RequestItemPropertyC2SPacket.class, NetworkDirection.PLAY_TO_SERVER);
        INSTANCE.messageBuilder(RequestItemPropertyC2S.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(RequestItemPropertyC2S::new)
            .encoder(RequestItemPropertyC2S::toBytes)
            .consumerMainThread(RequestItemPropertyC2S::handle)
            .add();

        // RECIPES
//        registerPacket(RecipeSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncRecipeS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncRecipeS2C::new)
            .encoder(SyncRecipeS2C::toBytes)
            .consumerMainThread(SyncRecipeS2C::handle)
            .add();

//        registerPacket(RecipeModSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncRecipeModS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncRecipeModS2C::new)
            .encoder(SyncRecipeModS2C::toBytes)
            .consumerMainThread(SyncRecipeModS2C::handle)
            .add();

        // ORES
//        registerPacket(OreSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncOreS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncOreS2C::new)
            .encoder(SyncOreS2C::toBytes)
            .consumerMainThread(SyncOreS2C::handle)
            .add();

        // MOB
//        registerPacket(MobSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncMobS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncMobS2C::new)
            .encoder(SyncMobS2C::toBytes)
            .consumerMainThread(SyncMobS2C::handle)
            .add();

        // DIMENSION
//        registerPacket(DimensionIdsSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncDimensionIdsS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncDimensionIdsS2C::new)
            .encoder(SyncDimensionIdsS2C::toBytes)
            .consumerMainThread(SyncDimensionIdsS2C::handle)
            .add();

        // STRUCTURE
        INSTANCE.messageBuilder(SyncStructureS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncStructureS2C::new)
            .encoder(SyncStructureS2C::toBytes)
            .consumerMainThread(SyncStructureS2C::handle)
            .add();

        INSTANCE.messageBuilder(SyncRestrictedStructuresS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncRestrictedStructuresS2C::new)
            .encoder(SyncRestrictedStructuresS2C::toBytes)
            .consumerMainThread(SyncRestrictedStructuresS2C::handle)
            .add();

        // SERVER
//        registerPacket(ServerStagesSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncServerStagesS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(SyncServerStagesS2C::new)
            .encoder(SyncServerStagesS2C::toBytes)
            .consumerMainThread(SyncServerStagesS2C::handle)
            .add();

        // SIMPLE
//        registerPacket(SimpleIdsSyncerS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(SyncSimpleIdsS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncSimpleIdsS2C::new)
                .encoder(SyncSimpleIdsS2C::toBytes)
                .consumerMainThread(SyncSimpleIdsS2C::handle)
                .add();

        // RELOADING
//        registerPacket(RequestReloadS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(RequestReloadS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RequestReloadS2C::new)
                .encoder(RequestReloadS2C::toBytes)
                .consumerMainThread(RequestReloadS2C::handle)
                .add();

//        registerPacket(RequestRestrictionDeleteS2CPacket.class, NetworkDirection.PLAY_TO_CLIENT);
        INSTANCE.messageBuilder(RequestRestrictionDeleteS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(RequestRestrictionDeleteS2C::new)
                .encoder(RequestRestrictionDeleteS2C::toBytes)
                .consumerMainThread(RequestRestrictionDeleteS2C::handle)
                .add();

        INSTANCE.messageBuilder(SendClientModelsC2S.class, id(), NetworkDirection.PLAY_TO_SERVER)
            .decoder(SendClientModelsC2S::new)
            .encoder(SendClientModelsC2S::toBytes)
            .consumerMainThread(SendClientModelsC2S::handle)
            .add();

        INSTANCE.messageBuilder(LogUnknowModelsS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(LogUnknowModelsS2C::new)
            .encoder(LogUnknowModelsS2C::toBytes)
            .consumerMainThread(LogUnknowModelsS2C::handle)
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
            Networking.sendToAllPlayers(message);
        } else {
            Networking.sendToPlayer(player, message);
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
