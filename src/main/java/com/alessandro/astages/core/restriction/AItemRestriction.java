package com.alessandro.astages.core.restriction;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.Attribute;
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
    public <T> AItemRestriction setAttribute(Attribute<T> attribute, T value) {
        var toReturn = super.setAttribute(attribute, value);

        if (attribute == Attributes.STORING_IN_INVENTORY || attribute == Attributes.EQUIPPING) {
            setChanged();
        }

        return toReturn;
    }

    @Override
    public void setChanged() {
        ARestrictionManager.ITEM_INSTANCE.reloadInventoryAndEquipmentRestrictions(this);
    }

    @Override
    public void markAsDirty() { }

    @SuppressWarnings("unused")
    public AItemRestriction setPickUpDelay(int value) {
        setAttribute(Attributes.PICK_UP_DELAY, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanAttack(boolean value) {
        setAttribute(Attributes.ATTACKING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanBeStoredInInventory(boolean value) {
        setAttribute(Attributes.STORING_IN_INVENTORY, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanBeEquipped(boolean value) {
        setAttribute(Attributes.EQUIPPING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanPickedUp(boolean value) {
        setAttribute(Attributes.PICKING_UP, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setHideTooltip(boolean value) {
        setAttribute(Attributes.HIDING_TOOLTIP, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setRenderItemName(boolean value) {
        setAttribute(Attributes.RENDERING_NAME, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setHideInJEI(boolean value) {
        setAttribute(Attributes.HIDING_JEI, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanBePlaced(boolean value) {
        setAttribute(Attributes.BLOCK_PLACING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanBeDig(boolean value) {
        setAttribute(Attributes.BLOCK_BREAKING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanItemBeLeftClicked(boolean value) {
        setAttribute(Attributes.LEFT_CLICK_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanItemBeRightClicked(boolean value) {
        setAttribute(Attributes.LEFT_CLICK_INTERACTIONS, value);
        return this;
    }

    @Override
    public String toString() {
        return this.getId() + " " + this.getAttribute(Attributes.STORING_IN_INVENTORY);
    }
}
