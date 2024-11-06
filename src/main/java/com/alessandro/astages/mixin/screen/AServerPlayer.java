package com.alessandro.astages.mixin.screen;

import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.OptionalInt;

@Mixin(value = ServerPlayer.class)
public class AServerPlayer {
    @Shadow public int containerCounter;

    @Shadow public ServerGamePacketListenerImpl connection;

    @Unique
    private ServerPlayer serverPlayer$self() {
        return (ServerPlayer) (Object) this;
    }

    @Inject(method = "openMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void astages$openMenu(MenuProvider pMenu, CallbackInfoReturnable<OptionalInt> cir, @NotNull AbstractContainerMenu abstractcontainermenu) {
        var restriction = ARestrictionManager.SCREEN_INSTANCE.getRestriction(serverPlayer$self(), abstractcontainermenu.getType());

        if (restriction != null) {
            cir.setReturnValue(OptionalInt.of(containerCounter));
        }
    }
}
