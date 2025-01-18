package com.alessandro.astages.core.restriction;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.ARestriction;
import com.alessandro.astages.store.Attribute;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AChangeable;
import com.alessandro.astages.util.AMarkable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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
            .addAttribute(Attributes.BLOCK_INTERACTIONS)

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
//            .addAttribute(Attributes.Item.A_USING_MESSAGE);
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
    public <T> AItemRestriction set(Attribute<T> attribute, T value) {
        var toReturn = super.set(attribute, value);

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
        set(Attributes.PICK_UP_DELAY, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanAttack(boolean value) {
        set(Attributes.ATTACKING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanBeStoredInInventory(boolean value) {
        set(Attributes.STORING_IN_INVENTORY, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanBeEquipped(boolean value) {
        set(Attributes.EQUIPPING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanPickedUp(boolean value) {
        set(Attributes.PICKING_UP, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setHideTooltip(boolean value) {
        set(Attributes.HIDING_TOOLTIP, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setRenderItemName(boolean value) {
        set(Attributes.RENDERING_NAME, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setHideInJEI(boolean value) {
        set(Attributes.HIDING_JEI, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanBePlaced(boolean value) {
        set(Attributes.BLOCK_PLACING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanBeDig(boolean value) {
        set(Attributes.BLOCK_BREAKING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanItemBeLeftClicked(boolean value) {
        set(Attributes.LEFT_CLICK_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanItemBeRightClicked(boolean value) {
        set(Attributes.LEFT_CLICK_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setCanInteractWithBlock(boolean value) {
        set(Attributes.BLOCK_INTERACTIONS, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setDropMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.DROP_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setAttackMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.ATTACK_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setPickupMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.PICKING_UP_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setUsageMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.USING_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setMineMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.MINING_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setPlaceMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.PLACING_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setJadeItemMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.JADE_ITEM_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public AItemRestriction setJadeBlockMessage(Function<ItemStack, Component> message) {
        set(Attributes.Item.JADE_BLOCK_MESSAGE, message);
        return this;
    }
    @Override
    public String toString() {
        return this.getId() + " " + this.get(Attributes.STORING_IN_INVENTORY);
    }
}
