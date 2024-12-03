package com.alessandro.astages.mixin.enchant;

import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class AEnchantment {
//    @Inject(method = "getDescriptionId", at = @At("HEAD"))
//    private void astages$getDescriptionId(CallbackInfoReturnable<String> cir) {
//
//    }
}
