package com.alessandro.astages.mixin.reload;

import com.alessandro.astages.core.AClientRestrictionManager;
import mezz.jei.library.startup.JeiStarter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JeiStarter.class, remap = false)
public class AJeiStarter {
    @Inject(method = "start", at = @At("HEAD"))
    public void astages$start(CallbackInfo ci) {
        AClientRestrictionManager.jeiStartedReload();
    }

    @Inject(method = "start", at = @At("TAIL"))
    public void astages$stop(CallbackInfo ci) {
        AClientRestrictionManager.jeiFinishedReload();
    }
}
