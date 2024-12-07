package com.alessandro.astages.event.enchant;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.RegisterStructureConversionsEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class ServerEventHandler {
    // ENCHANTMENT AND POTION DRINKING EFFECTS

    public static void c(RegisterStructureConversionsEvent event) {

    }

    public static void a(EnchantmentLevelSetEvent event) {

    }

    public static void d(MobEffectEvent.@NotNull Added event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getEffectInstance().getEffect() == ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(""))) {
                player.removeEffect(event.getEffectInstance().getEffect());
            }
        }
    }

//    public static void e(ChunkGenerationEvent event) {
//        event.
//    }

    public static void e() {
    }

    public static void onEnter(PlayerBrewedPotionEvent event) {
        // event.getEntity().getActiveEffects().
    }

    public static void onAnvilUpdate(@NotNull AnvilUpdateEvent event) {
//        event.setCost();

        if (event.getRight().getItem() instanceof EnchantedBookItem bookItem) {
            bookItem.getEnchantmentLevel(event.getRight(), ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("")));
        }
    }
}
