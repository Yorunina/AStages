package com.alessandro.astages.core.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.restriction.AItemRestriction;
import com.alessandro.astages.store.AManager;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class AItemManager extends AManager<AItemRestriction, Predicate<ItemStack>, ItemStack> {
    public final Map<String, List<AItemRestriction>> equipmentRestrictions = new HashMap<>();
    public final Map<String, List<AItemRestriction>> inventoryRestrictions = new HashMap<>();

    @Override
    public AItemRestriction getRestriction(Player player, ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        return super.getRestriction(player, stack);
    }

    @Override
    public void addRestriction(AItemRestriction restriction) {
        super.addRestriction(restriction);

        if (restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
            var newInventoryList = inventoryRestrictions.getOrDefault(restriction.getStage(), new ArrayList<>());
            if (!newInventoryList.isEmpty()) { newInventoryList.removeIf(rest -> Objects.equals(rest.getId(), restriction.getId())); }
            newInventoryList.add(restriction);

            inventoryRestrictions.put(restriction.getStage(), newInventoryList);
        }

        if (restriction.isDisabled(Attributes.EQUIPPING)) {
            var newEquipmentList = equipmentRestrictions.getOrDefault(restriction.getStage(), new ArrayList<>());
            if (!newEquipmentList.isEmpty()) { newEquipmentList.removeIf(rest -> Objects.equals(rest.getId(), restriction.getId())); }
            newEquipmentList.add(restriction);

            equipmentRestrictions.put(restriction.getStage(), newEquipmentList);
        }
    }

    public void reloadInventoryAndEquipmentRestrictions(AItemRestriction restriction) {
        inventoryRestrictions.getOrDefault(restriction.getStage(), new ArrayList<>()).removeIf(r -> Objects.equals(r.getId(), restriction.getId()));
        equipmentRestrictions.getOrDefault(restriction.getStage(), new ArrayList<>()).removeIf(r -> Objects.equals(r.getId(), restriction.getId()));

        if (restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
            var newInventoryList = inventoryRestrictions.getOrDefault(restriction.getStage(), new ArrayList<>());
            if (!newInventoryList.isEmpty()) { newInventoryList.removeIf(rest -> Objects.equals(rest.getId(), restriction.getId())); }
            newInventoryList.add(restriction);

            AStages.LOGGER.debug(restriction.getId());

            inventoryRestrictions.put(restriction.getStage(), newInventoryList);
        }

        if (restriction.isDisabled(Attributes.EQUIPPING)) {
            var newEquipmentList = equipmentRestrictions.getOrDefault(restriction.getStage(), new ArrayList<>());
            if (!newEquipmentList.isEmpty()) { newEquipmentList.removeIf(rest -> Objects.equals(rest.getId(), restriction.getId())); }
            newEquipmentList.add(restriction);

            equipmentRestrictions.put(restriction.getStage(), newEquipmentList);
        }
    }

    public AItemRestriction getRestriction(ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        for (String stage : restrictions.keySet()) {
            for (AItemRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(stack)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public AItemRestriction getInventoryRestriction(Player player, ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        for (String stage : inventoryRestrictions.keySet()) {
            for (AItemRestriction restriction : inventoryRestrictions.get(stage)) {
                if (restriction.isRestricted(stack) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public AItemRestriction getEquipmentRestriction(Player player, ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        for (String stage : equipmentRestrictions.keySet()) {
            for (AItemRestriction restriction : equipmentRestrictions.get(stage)) {
                if (restriction.isRestricted(stack) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
