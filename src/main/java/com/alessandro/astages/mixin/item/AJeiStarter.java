package com.alessandro.astages.mixin.item;

import com.alessandro.astages.event.item.ClientEventHandler;
import mezz.jei.library.startup.JeiStarter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JeiStarter.class, remap = false)
public class AJeiStarter {
    @Inject(method = "start", at = @At(value = "INVOKE", target = "Lmezz/jei/core/util/LoggedTimer;start(Ljava/lang/String;)V"))
    public void start(CallbackInfo ci) {
        ClientEventHandler.jeiGetter = true;
    }

    @Inject(method = "start", at = @At(value = "INVOKE", target = "Lmezz/jei/core/util/LoggedTimer;stop()V"))
    public void stop(CallbackInfo ci) {
        ClientEventHandler.jeiGetter = false;
    }
}
