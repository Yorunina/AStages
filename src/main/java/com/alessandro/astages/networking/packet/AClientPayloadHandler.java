package com.alessandro.astages.networking.packet;

import com.alessandro.astages.Astages;
import com.alessandro.astages.capability.ClientPlayerStage;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class AClientPayloadHandler {
    public static void handleDataOnMain(final @NotNull StageDataSyncS2CPacket data, final @NotNull IPayloadContext context) {
//        context.enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
        Astages.LOGGER.debug("CLIENT UPDATED!");
        ClientPlayerStage.set(data.stages());
        Astages.LOGGER.debug("CLIENT UPDATED!");
//        });
    }
}
