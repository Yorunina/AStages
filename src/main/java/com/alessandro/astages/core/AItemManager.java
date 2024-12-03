package com.alessandro.astages.core;

import com.alessandro.astages.util.AManager;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.Info;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AItemManager implements AManager<AItemRestriction, ItemStack> {
//    public static boolean isInventoryChanged = false;

    public final Map<String, List<AItemRestriction>> restrictions = new HashMap<>();
    public final Map<String, List<AItemRestriction>> equipmentRestrictions = new HashMap<>();
    public final Map<String, List<AItemRestriction>> inventoryRestrictions = new HashMap<>();

    public Map<String, List<AItemRestriction>> getRestrictions() {
        return restrictions;
    }

    @Override
    public void reloadBeforeScripts() {
        restrictions.clear();
    }

    @Override
    public void addRestriction(String stage, AItemRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());
        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
        newList.add(restriction);
        restrictions.put(stage, newList);

        if (!restriction.canBeStoredInInventory) {
            var newInventoryList = inventoryRestrictions.getOrDefault(stage, new ArrayList<>());
            if (!newInventoryList.isEmpty()) { newInventoryList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
            newInventoryList.add(restriction);

            inventoryRestrictions.put(stage, newInventoryList);
        }

        if (!restriction.canBeEquipped) {
            var newEquipmentList = equipmentRestrictions.getOrDefault(stage, new ArrayList<>());
            if (!newEquipmentList.isEmpty()) { newEquipmentList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
            newEquipmentList.add(restriction);

            equipmentRestrictions.put(stage, newEquipmentList);
        }

        ARestrictionManager.ALL_STAGES.add(stage);
    }

//    @Info("Implement an optimized version of this method")
    public void reloadInventoryAndEquipmentRestrictions(@NotNull AItemRestriction restriction) {
        inventoryRestrictions.get(restriction.stage).removeIf(r -> Objects.equals(r.id, restriction.id));
        equipmentRestrictions.get(restriction.stage).removeIf(r -> Objects.equals(r.id, restriction.id));

        if (!restriction.canBeStoredInInventory) {
            var newInventoryList = inventoryRestrictions.getOrDefault(restriction.stage, new ArrayList<>());
            if (!newInventoryList.isEmpty()) { newInventoryList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
            newInventoryList.add(restriction);

            inventoryRestrictions.put(restriction.stage, newInventoryList);
        }

        if (!restriction.canBeEquipped) {
            var newEquipmentList = equipmentRestrictions.getOrDefault(restriction.stage, new ArrayList<>());
            if (!newEquipmentList.isEmpty()) { newEquipmentList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
            newEquipmentList.add(restriction);

            equipmentRestrictions.put(restriction.stage, newEquipmentList);
        }

//        equipmentRestrictions.clear();
//        inventoryRestrictions.clear();
//
//        for (var entry : restrictions.entrySet()) {
//            for (var restriction : entry.getValue()) {
//                if (!restriction.canBeStoredInInventory) {
//                    var newInventoryList = inventoryRestrictions.getOrDefault(entry.getKey(), new ArrayList<>());
//                    if (!newInventoryList.isEmpty()) { newInventoryList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
//                    newInventoryList.add(restriction);
//
//                    inventoryRestrictions.put(entry.getKey(), newInventoryList);
//                }
//
//                if (!restriction.canBeEquipped) {
//                    var newEquipmentList = equipmentRestrictions.getOrDefault(entry.getKey(), new ArrayList<>());
//                    if (!newEquipmentList.isEmpty()) { newEquipmentList.removeIf(rest -> Objects.equals(rest.id, restriction.id)); }
//                    newEquipmentList.add(restriction);
//
//                    equipmentRestrictions.put(entry.getKey(), newEquipmentList);
//                }
//            }
//        }
    }

    @Override
    public AItemRestriction getRestriction(String id) {
        for (String stage : restrictions.keySet()) {
            for (AItemRestriction restriction : restrictions.get(stage)) {
                if (restriction.id.equals(id)) {
                    return restriction;
                }
            }
        }

        return null;
    }

//    @Override
    public AItemRestriction getRestriction(@NotNull ItemStack stack) {
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

    /**
     * Get the restriction for a particular item.
     * If returns null, there isn't any restriction for the item.
     *
     * @param stack item to check
     * @return restriction or null
     */
    @Override
    public AItemRestriction getRestriction(Player player, @NotNull ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        for (String stage : restrictions.keySet()) {
            for (AItemRestriction restriction : restrictions.get(stage)) {
                if (restriction.isRestricted(stack) && !AStagesUtil.hasStage(player, stage)) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public AItemRestriction getInventoryRestriction(Player player, @NotNull ItemStack stack) {
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

    public AItemRestriction getEquipmentRestriction(Player player, @NotNull ItemStack stack) {
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
