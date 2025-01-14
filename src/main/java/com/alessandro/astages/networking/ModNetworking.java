package com.alessandro.astages.networking;

import com.alessandro.astages.AStages;
import com.alessandro.astages.networking.packet.RenderAtLoginS2CPacket;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.alessandro.astages.networking.packet.syncer.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = AStages.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetworking {
    @SubscribeEvent
    public static void register(@NotNull RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.NETWORK);

        // STAGES
        registrar.playToClient(StageDataSyncS2CPacket.TYPE, StageDataSyncS2CPacket.STREAM_CODEC, StageDataSyncS2CPacket::handle);
        registrar.playToClient(RenderAtLoginS2CPacket.TYPE, RenderAtLoginS2CPacket.STREAM_CODEC, RenderAtLoginS2CPacket::handle);

        // ITEM
        registrar.playToServer(IsItemRestrictedC2SPacket.TYPE, IsItemRestrictedC2SPacket.STREAM_CODEC, IsItemRestrictedC2SPacket::handle);
        registrar.playToClient(ItemSyncerS2CPacket.TYPE, ItemSyncerS2CPacket.STREAM_CODEC, ItemSyncerS2CPacket::handle);
        registrar.playToClient(NullItemSyncerS2CPacket.TYPE, NullItemSyncerS2CPacket.STREAM_CODEC, NullItemSyncerS2CPacket::handle);

        // JEI
        registrar.playToServer(IsJeiRestrictedC2SPacket.TYPE, IsJeiRestrictedC2SPacket.STREAM_CODEC, IsJeiRestrictedC2SPacket::handle);
        registrar.playToClient(JeiIsRestrictedS2CPacket.TYPE, JeiIsRestrictedS2CPacket.STREAM_CODEC, JeiIsRestrictedS2CPacket::handle);
        registrar.playToClient(JeiSyncerS2CPacket.TYPE, JeiSyncerS2CPacket.STREAM_CODEC, JeiSyncerS2CPacket::handle);
        registrar.playToClient(RequestJeiClientReloadS2CPacket.TYPE, RequestJeiClientReloadS2CPacket.STREAM_CODEC, RequestJeiClientReloadS2CPacket::handle);

        // RECIPES
        registrar.playToClient(JeiRecipeSyncerS2CPacket.TYPE, JeiRecipeSyncerS2CPacket.STREAM_CODEC, JeiRecipeSyncerS2CPacket::handle);

        // ORES
        registrar.playToClient(OreSyncerS2CPacket.TYPE, OreSyncerS2CPacket.STREAM_CODEC, OreSyncerS2CPacket::handle);
        registrar.playToClient(OreStagesSyncerS2CPacket.TYPE, OreStagesSyncerS2CPacket.STREAM_CODEC, OreStagesSyncerS2CPacket::handle);

        // RELOADING
        registrar.playToClient(RequestClientReloadS2CPacket.TYPE, RequestClientReloadS2CPacket.STREAM_CODEC, RequestClientReloadS2CPacket::handle);

        // AREA PROTECTED
//        registrar.playToServer(IsItemRestrictedC2SPacket.TYPE, IsItemRestrictedC2SPacket.STREAM_CODEC, IsItemRestrictedC2SPacket::handle);
    }
}
