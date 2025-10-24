package com.alessandro.astages.loot;

import com.alessandro.astages.AStages;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AStages.MODID);

    // public static final RegistryObject<Codec<ALootModifier>> STAGE_LOOT_MODIFIER_CODEC = MODIFIERS.register("stage_loot_modifier", () -> ALootModifier.CODEC);
}
