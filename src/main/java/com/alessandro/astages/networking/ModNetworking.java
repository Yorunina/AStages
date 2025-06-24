package com.alessandro.astages.networking;

import com.alessandro.astages.AStages;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.alessandro.astages.networking.packet.dimension.DimensionIdsSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.*;
import com.alessandro.astages.networking.packet.mob.MobSyncerS2CPacket;
import com.alessandro.astages.networking.packet.ore.OreSyncerS2CPacket;
import com.alessandro.astages.networking.packet.recipe.RecipeModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.recipe.RecipeSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestRestrictionDeleteS2CPacket;
import com.alessandro.astages.networking.packet.server.ServerStagesSyncerS2CPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = AStages.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetworking {
    @SubscribeEvent
    public static void register(@NotNull RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.NETWORK);

        // STAGES
        registrar.playToClient(StageDataSyncS2CPacket.TYPE, StageDataSyncS2CPacket.STREAM_CODEC, StageDataSyncS2CPacket::handle);

        // ITEMS
        registrar.playToClient(ItemSyncerS2CPacket.TYPE, ItemSyncerS2CPacket.STREAM_CODEC, ItemSyncerS2CPacket::handle);
        registrar.playToClient(ItemTagSyncerS2CPacket.TYPE, ItemTagSyncerS2CPacket.STREAM_CODEC, ItemTagSyncerS2CPacket::handle);
        registrar.playToClient(ItemModSyncerS2CPacket.TYPE, ItemModSyncerS2CPacket.STREAM_CODEC, ItemModSyncerS2CPacket::handle);
        registrar.playToClient(ItemPredicateSyncerS2CPacket.TYPE, ItemPredicateSyncerS2CPacket.STREAM_CODEC, ItemPredicateSyncerS2CPacket::handle);
        registrar.playToClient(ItemPropertySyncerS2CPacket.TYPE, ItemPropertySyncerS2CPacket.STREAM_CODEC, ItemPropertySyncerS2CPacket::handle);
        registrar.playToServer(RequestItemPropertyC2SPacket.TYPE, RequestItemPropertyC2SPacket.STREAM_CODEC, RequestItemPropertyC2SPacket::handle);

        // RECIPE
        registrar.playToClient(RecipeSyncerS2CPacket.TYPE, RecipeSyncerS2CPacket.STREAM_CODEC, RecipeSyncerS2CPacket::handle);
        registrar.playToClient(RecipeModSyncerS2CPacket.TYPE, RecipeModSyncerS2CPacket.STREAM_CODEC, RecipeModSyncerS2CPacket::handle);

        // ORES
        registrar.playToClient(OreSyncerS2CPacket.TYPE, OreSyncerS2CPacket.STREAM_CODEC, OreSyncerS2CPacket::handle);

        // MOB
        registrar.playToClient(MobSyncerS2CPacket.TYPE, MobSyncerS2CPacket.STREAM_CODEC, MobSyncerS2CPacket::handle);

        // DIMENSION
        registrar.playToClient(DimensionIdsSyncerS2CPacket.TYPE, DimensionIdsSyncerS2CPacket.STREAM_CODEC, DimensionIdsSyncerS2CPacket::handle);

        // SERVER
        registrar.playToClient(ServerStagesSyncerS2CPacket.TYPE, ServerStagesSyncerS2CPacket.STREAM_CODEC, ServerStagesSyncerS2CPacket::handle);

        // RELOADING
        registrar.playToClient(RequestReloadS2CPacket.TYPE, RequestReloadS2CPacket.STREAM_CODEC, RequestReloadS2CPacket::handle);
        registrar.playToClient(RequestRestrictionDeleteS2CPacket.TYPE, RequestRestrictionDeleteS2CPacket.STREAM_CODEC, RequestRestrictionDeleteS2CPacket::handle);
    }

    public static void sendTo(@Nullable ServerPlayer player, CustomPacketPayload payload) {
        if (player == null) { // If Null -> Whole Server!
            PacketDistributor.sendToAllPlayers(payload);
        } else {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }
}
