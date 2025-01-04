package com.alessandro.astages.core.restriction;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AChangeable;
import com.alessandro.astages.util.AMarkable;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AItemRestriction extends ARestriction<AItemRestriction, Predicate<ItemStack>, ItemStack> implements AChangeable, AMarkable {
    private final List<Predicate<ItemStack>> predicates = new ArrayList<>();

    public AItemRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
            .addAttribute(Attributes.RENDERING_NAME)
            .addAttribute(Attributes.HIDING_TOOLTIP)
            .addAttribute(Attributes.PICKING_UP)
            .addAttribute(Attributes.EQUIPPING)
            .addAttribute(Attributes.STORING_IN_INVENTORY)
            .addAttribute(Attributes.ATTACKING)
            .addAttribute(Attributes.HIDING_JEI)
            .addAttribute(Attributes.BLOCK_PLACING)
            .addAttribute(Attributes.LEFT_CLICK_INTERACTIONS)
            .addAttribute(Attributes.RIGHT_CLICK_INTERACTIONS)
            .addAttribute(Attributes.BLOCK_BREAKING)

            .addAttribute(Attributes.PICK_UP_DELAY)

            .addAttribute(Attributes.Item.HIDDEN_NAME)
            .addAttribute(Attributes.Item.DROP_MESSAGE)
            .addAttribute(Attributes.Item.ATTACK_MESSAGE)
            .addAttribute(Attributes.Item.PICKING_UP_MESSAGE)
            .addAttribute(Attributes.Item.USING_MESSAGE)
            .addAttribute(Attributes.Item.MINING_MESSAGE)
            .addAttribute(Attributes.Item.PLACING_MESSAGE)
            .addAttribute(Attributes.Item.JADE_ITEM_MESSAGE)
            .addAttribute(Attributes.Item.JADE_BLOCK_MESSAGE);
    }

    @Override
    public AItemRestriction restrict(Predicate<ItemStack> predicate) {
        predicates.add(predicate);

        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        for (Predicate<ItemStack> predicate : predicates) {
            if (predicate.test(stack)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void setChanged() {
        ARestrictionManager.ITEM_INSTANCE.reloadInventoryAndEquipmentRestrictions(this);
    }

    @Override
    public void markAsDirty() { }
}
