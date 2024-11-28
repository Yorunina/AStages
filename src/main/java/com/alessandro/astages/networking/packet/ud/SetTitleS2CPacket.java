package com.alessandro.astages.networking.packet.ud;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SetTitleS2CPacket {
    private final Component title;
    private final Component subtitle;
    private final int fadeIn;
    private final int fadeOut;
    private final int stay;

    public SetTitleS2CPacket(Component component, Component subtitle, int fadeIn, int fadeOut, int stay) {
        title = component;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.stay = stay;
    }

    public SetTitleS2CPacket(@NotNull FriendlyByteBuf buf) {
        title = buf.readComponent();
        subtitle = buf.readComponent();
        fadeIn = buf.readInt();
        fadeOut = buf.readInt();
        stay = buf.readInt();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeComponent(title);
        buf.writeComponent(subtitle);
        buf.writeInt(fadeIn);
        buf.writeInt(fadeOut);
        buf.writeInt(stay);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Minecraft.getInstance().gui.setTimes(fadeIn, fadeOut, stay);
            Minecraft.getInstance().gui.setTimes(fadeIn, stay, fadeOut);
            Minecraft.getInstance().gui.setTitle(title);
            Minecraft.getInstance().gui.setTitle(subtitle);
        });

        ctx.get().setPacketHandled(true);
    }
}
