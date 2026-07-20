package com.alessandro.astages.infrastructure.datagen;

import com.alessandro.astages.AStages;
import com.alessandro.astages.infrastructure.loot.modifier.ALootModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ALootProvider extends GlobalLootModifierProvider {
    public ALootProvider(PackOutput output) {
        super(output, AStages.MODID);
    }

    @Override
    protected void start() {
        // Applied to all loot tables! (Blocks, entities and so on!)
        add("astages_loot_modifier_instance", new ALootModifier(new LootItemCondition[]{ }));
    }
}
