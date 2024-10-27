package com.alessandro.astages.networking.packet;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.ore.ClientEventHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class RenderAtLoginS2CPacket {
    public RenderAtLoginS2CPacket() { }

    public RenderAtLoginS2CPacket(@NotNull FriendlyByteBuf buf) { }

    public void toBytes(@NotNull FriendlyByteBuf buf) { }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(ClientEventHandler::renderAllAgain);

        ctx.get().setPacketHandled(true);
    }
}
