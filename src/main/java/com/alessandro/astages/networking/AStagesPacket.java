package com.alessandro.astages.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface AStagesPacket /*<T>*/ {
//    T instantiate();
//    T read();
    void toBytes(FriendlyByteBuf buf);
    void handle(Supplier<NetworkEvent.Context> ctx);
}
