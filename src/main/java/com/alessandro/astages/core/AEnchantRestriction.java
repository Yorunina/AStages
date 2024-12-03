package com.alessandro.astages.core;

import com.alessandro.astages.util.ACompareCondition;
import com.alessandro.astages.util.ARestriction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;

public class AEnchantRestriction implements ARestriction {
    public final String id;

    public Enchantment enchantment;
    public ACompareCondition compareCondition;
    public int level;

    public boolean isAnvilRestricted = true;
    public boolean isEnchantingTableRestricted = true;

    public AEnchantRestriction(String id) {
        this.id = id;
    }

    public AEnchantRestriction restrict(Enchantment enchantment, ACompareCondition compareCondition, int level) {
        this.enchantment = enchantment;
        this.compareCondition = compareCondition;
        this.level = level;

        return this;
    }

    public boolean isRestricted(Enchantment enchantment) {
        return this.enchantment.equals(enchantment);
    }
}
