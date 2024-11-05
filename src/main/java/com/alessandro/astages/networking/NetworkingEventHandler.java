package com.alessandro.astages.networking;

import com.alessandro.astages.Astages;
import com.alessandro.astages.networking.packet.AClientPayloadHandler;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handlers.ClientPayloadHandler;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Astages.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkingEventHandler {
    @SubscribeEvent
    public static void register(@NotNull RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.MAIN);
        registrar.playToClient(
            StageDataSyncS2CPacket.TYPE,
            StageDataSyncS2CPacket.STREAM_CODEC,
//            new DirectionalPayloadHandler<>(
//                AClientPayloadHandler::handleDataOnMain,
//                AClientPayloadHandler::handleDataOnMain
//            )
            AClientPayloadHandler::handleDataOnMain
        );
    }
}
