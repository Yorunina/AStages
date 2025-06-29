package com.alessandro.astages.mixin.reload;

import com.alessandro.astages.core.AClientRestrictionManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.ReloadCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ReloadCommand.class)
public class AReloadCommand {
    @Inject(method = "reloadPacks", at = @At("HEAD"))
    private static void astages$reloadPacks(Collection<String> ids, CommandSourceStack source, CallbackInfo ci) {
        AClientRestrictionManager.reloadStarted();
    }
}
