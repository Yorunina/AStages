package com.alessandro.astages.core;

import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AEnchantManager implements AManager<AEnchantRestriction, AEnchantManager.EnchantWrapper> {
    public final Map<String, List<AEnchantRestriction>> restrictions = new HashMap<>();

    @Override
    public void addRestriction(String stage, AEnchantRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());

        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);

        ARestrictionManager.ALL_STAGES.add(stage);

        restrictions.put(stage, newList);
    }

    @Override
    public AEnchantRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (AEnchantRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public AEnchantRestriction getRestriction(Player player, EnchantWrapper wrapper) {
        for (String stage : restrictions.keySet()) {
            for (AEnchantRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(wrapper.enchantment) && elaborateRestriction(restriction, wrapper.level) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    @Override
    public void reloadBeforeScripts() {
        restrictions.clear();
    }

    public boolean elaborateRestriction(@NotNull AEnchantRestriction restriction, int level) {
        return switch (restriction.compareCondition) {
            case EQUAL -> restriction.level == level;
            case LESS -> level < restriction.level;
            case LESS_EQUAL -> level <= restriction.level;
            case GREAT -> level > restriction.level;
            case GREAT_EQUAL -> level >= restriction.level;
        };
    }

    // public record EnchantWrapper(Enchantment enchantment, ACompareCondition compareCondition, int level) { }
    public record EnchantWrapper(Enchantment enchantment, int level) { }
}
