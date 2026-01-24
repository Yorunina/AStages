package com.alessandro.astages.mixin.food;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.event.custom.LivingEntityEatEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ALivingEntity {
    @Unique
    private LivingEntity livingEntity$self() {
        return (LivingEntity) (Object) this;
    }

    @Inject(method = "eat", at = @At("HEAD"))
    private void astages$eat(Level level, ItemStack food, CallbackInfoReturnable<ItemStack> cir) {
        ALoader.EVENT_BUS.post(new LivingEntityEatEvent(livingEntity$self(), food));
    }
}
