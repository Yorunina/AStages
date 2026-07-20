package com.alessandro.astages.infrastructure.mixin.integration.probejs;

import com.alessandro.astages.api.tag.AItemTag;
import com.google.common.collect.Multimap;
import com.probejs.specials.assign.ClassAssignmentManager;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.PrimitiveDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClassAssignmentManager.class, remap = false)
public class AClassAssignmentManager {
    @Shadow
    @Final public static Multimap<Class<?>, TypeDescJS> ASSIGNMENTS;

    @Inject(method = "init", at = @At("HEAD"))
    private static void astages$init(DescriptionContext context, CallbackInfo ci) {
        ASSIGNMENTS.put(AItemTag.class, new PrimitiveDescJS("`${Special.AItemTag}`"));
    }
}
