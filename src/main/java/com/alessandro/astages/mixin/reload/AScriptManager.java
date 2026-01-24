package com.alessandro.astages.mixin.reload;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.event.custom.ReloadScriptEvent;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@NotNullParams
@Mixin(value = ScriptManager.class, remap = false)
public class AScriptManager {
    @Shadow @Final public ScriptType scriptType;

    @Inject(method = "reload", at = @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/script/ScriptManager;loadFromDirectory()V"))
    public void astages$beforeScriptsLoaded(ResourceManager resourceManager, CallbackInfo ci) {
        ALoader.EVENT_BUS.post(new ReloadScriptEvent.BeforeScriptsLoaded(astages$conversion(scriptType)));
    }

    @Inject(method = "reload", at = @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/script/ScriptManager;load()V", shift = At.Shift.AFTER))
    public void astages$afterScriptsLoaded(ResourceManager resourceManager, CallbackInfo ci) {
        ALoader.EVENT_BUS.post(new ReloadScriptEvent.AfterScriptsLoaded(astages$conversion(scriptType)));
    }

    @Unique
    private static ReloadScriptEvent.EventScriptType astages$conversion(ScriptType scriptType) {
        return switch (scriptType) {
            case STARTUP -> ReloadScriptEvent.EventScriptType.STARTUP;
            case SERVER -> ReloadScriptEvent.EventScriptType.SERVER;
            case CLIENT -> ReloadScriptEvent.EventScriptType.CLIENT;
        };
    }
}
