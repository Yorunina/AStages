package com.alessandro.astages.mixin.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientItemUpdateEvent;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.alessandro.astages.event.item.ClientEventHandler;
import mezz.jei.library.startup.JeiStarter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = JeiStarter.class, remap = false)
public class AJeiStarter {
    @Inject(method = "start", at = @At(value = "INVOKE", target = "Lmezz/jei/core/util/LoggedTimer;start(Ljava/lang/String;)V"))
    public void astages$start(CallbackInfo ci) {
        ClientEventHandler.jeiGetter = true;
    }

    @Inject(method = "start", at = @At(value = "INVOKE", target = "Lmezz/jei/common/Internal;setRuntime(Lmezz/jei/api/runtime/IJeiRuntime;)V", shift = At.Shift.AFTER))
    public void start(CallbackInfo ci) {
        AStages.LOGGER.debug(EffectiveSide.get().name());
        AClientRestrictionManager.jeiIsReloading = false;
        MinecraftForge.EVENT_BUS.post(new ClientItemUpdateEvent());
        MinecraftForge.EVENT_BUS.post(new ClientRecipeUpdateEvent());
    }

    @Inject(method = "start", at = @At(value = "INVOKE", target = "Lmezz/jei/core/util/LoggedTimer;stop()V"))
    public void astages$stop(CallbackInfo ci) {
        ClientEventHandler.jeiGetter = false;
    }
}
