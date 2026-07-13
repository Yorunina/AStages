package com.alessandro.astages.infrastructure.mixin.integration.probejs;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.integration.probejs.AItemTagFormatter;
import com.llamalad7.mixinextras.sugar.Local;
import com.probejs.docs.formatter.formatter.IFormatter;
import com.probejs.specials.SpecialCompiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@NotNullParams
@Mixin(value = SpecialCompiler.class, remap = false)
public class ASpecialCompiler {
    @Inject(method = "compileSpecial", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private static void astages$compileSpecial(CallbackInfoReturnable<List<IFormatter>> cir, @Local(name = "formatters") List<IFormatter> formatters) {
        formatters.add(new AItemTagFormatter());
    }
}
