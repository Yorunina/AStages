package com.alessandro.astages.infrastructure.mixin.loot;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.loot.ALootProcessor;
import com.almostreliable.lootjs.LootModificationsAPI;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@NotNullParams
@Mixin(value = LootModificationsAPI.class, remap = false)
public class ALootModificationsAPI {
    @Inject(method = "invokeActions", at = @At("TAIL"))
    private static void astages$runModifiers(List<ItemStack> loot, LootContext context, CallbackInfo ci) {
        ALootProcessor.apply(loot.listIterator(), context);
    }
}