package com.alessandro.astages.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface AStagesPacket extends CustomPacketPayload {
    void handle(IPayloadContext context);
}
