package com.alessandro.astages.mixin.screen;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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
import java.util.function.Consumer;

@Mixin(value = ServerPlayer.class)
public class AServerPlayer {
    @Shadow private int containerCounter;

    @Unique
    private ServerPlayer serverPlayer$self() {
        return (ServerPlayer) (Object) this;
    }

    @Inject(method = "openMenu(Lnet/minecraft/world/MenuProvider;Ljava/util/function/Consumer;)Ljava/util/OptionalInt;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V", shift = At.Shift.AFTER, by = 1), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void astages$openMenu1(MenuProvider menu, Consumer<RegistryFriendlyByteBuf> extraDataWriter, CallbackInfoReturnable<OptionalInt> cir, @NotNull AbstractContainerMenu abstractcontainermenu) {
        astages$do(cir, abstractcontainermenu);
    }

    @Inject(method = "openMenu(Lnet/minecraft/world/MenuProvider;Ljava/util/function/Consumer;)Ljava/util/OptionalInt;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;)V", shift = At.Shift.AFTER, by = 1), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void astages$openMenu2(MenuProvider menu, Consumer<RegistryFriendlyByteBuf> extraDataWriter, CallbackInfoReturnable<OptionalInt> cir, @NotNull AbstractContainerMenu abstractcontainermenu) {
        astages$do(cir, abstractcontainermenu);
    }

    @Unique
    private void astages$do(CallbackInfoReturnable<OptionalInt> cir, @NotNull AbstractContainerMenu abstractcontainermenu) {
        var restriction = ARestrictionManager.SCREEN_INSTANCE.getRestriction(serverPlayer$self(), abstractcontainermenu.getType());

        if (restriction != null) {
            restriction.displayMessage(Attributes.Screen.OPEN_MESSAGE, abstractcontainermenu.getType(), serverPlayer$self());

            cir.setReturnValue(OptionalInt.of(containerCounter));
        }
    }
}
