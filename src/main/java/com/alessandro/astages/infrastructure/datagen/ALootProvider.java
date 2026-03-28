package com.alessandro.astages.infrastructure.datagen;

import com.alessandro.astages.AStages;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ALootProvider extends GlobalLootModifierProvider {
    public ALootProvider(PackOutput output) {
        super(output, AStages.MODID);
    }

    @Override
    protected void start() {
        // Applied to all loot tables! (Blocks, entities and so on!)
        // add("stage_loot_modifier_instance", new ALootModifier(new LootItemCondition[]{ }));
    }
}
