package com.alessandro.astages.core;

import com.alessandro.astages.util.AChangeable;
import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.Info;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class AItemRestriction implements ARestriction, AChangeable {
    public final String id;
    public final String stage;

    public boolean renderItemName = false;
    public boolean hideTooltip = true;
    public boolean canPickedUp = false;
    public boolean canBeEquipped = false;
    public boolean canBeStoredInInventory = false;
    public boolean canAttack = false;
    public boolean hideInJEI = true;
    public boolean canBePlaced = false;
//    @Info("To be reviewed!") public boolean canBlockBeUsed = false;
    @Info("To be reviewed!") public boolean canItemBeUsed = false;
    public boolean canBeDig = false;

    public Function<ItemStack, Component> hiddenName = stack -> Component.translatable("tooltip.astages.hidden_name", stack.getHoverName()).withStyle(ChatFormatting.RED);
    public Function<ItemStack, Component> dropMessage = stack -> Component.translatable("message.astages.drop", stack.getHoverName()).withStyle(ChatFormatting.RED);
    public Function<ItemStack, Component> attackMessage = stack -> Component.translatable("message.astages.attach", stack.getHoverName()).withStyle(ChatFormatting.RED);
    public Function<ItemStack, Component> pickupMessage = stack -> Component.translatable("message.astages.pickup", stack.getHoverName()).withStyle(ChatFormatting.RED);
    public Function<ItemStack, Component> usageMessage = stack -> Component.translatable("message.astages.use", stack.getHoverName()).withStyle(ChatFormatting.RED);
    public Function<ItemStack, Component> mineMessage = stack -> Component.translatable("message.astages.mine", stack.getHoverName()).withStyle(ChatFormatting.RED);
    public Function<ItemStack, Component> placeMessage = stack -> Component.translatable("message.astages.place", stack.getHoverName()).withStyle(ChatFormatting.RED);
    public Function<ItemStack, Component> jadeItemMessage = stack -> Component.translatable("tooltip.astages.jade_integration.item", stack.getHoverName()).withStyle(ChatFormatting.RED);
    public Function<ItemStack, Component> jadeBlockMessage = stack -> Component.translatable("tooltip.astages.jade_integration.block", stack.getHoverName()).withStyle(ChatFormatting.RED);

    public int pickUpDelay = 60;

    public List<Predicate<ItemStack>> predicates = new ArrayList<>();

    public AItemRestriction(String id, String stage) {
        this.id = id;
        this.stage = stage;
    }

    public AItemRestriction restrict(Predicate<ItemStack> predicate) {
        predicates.add(predicate);

        return this;
    }

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
    public String toString() {
        return "AItemRestriction{" +
            "id='" + id + '\'' +
            ", hideInJEI=" + hideInJEI +
            ", canAttack=" + canAttack +
            ", canBeStoredInInventory=" + canBeStoredInInventory +
            ", canBeEquipped=" + canBeEquipped +
            ", canPickedUp=" + canPickedUp +
            ", hideTooltip=" + hideTooltip +
            ", renderItemName=" + renderItemName +
            ", canBePlaced=" + canBePlaced +
//            ", canBlockBeUsed=" + canBlockBeUsed +
            ", canItemBeUsed=" + canItemBeUsed +
            ", pickUpDelay=" + pickUpDelay +
            '}';
    }

    // GETTER AND SETTERS
    public int getPickUpDelay() {
        return pickUpDelay;
    }

    public AItemRestriction setPickUpDelay(int pickUpDelay) {
        this.pickUpDelay = pickUpDelay;

        return this;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public AItemRestriction setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;

        return this;
    }

    public boolean isCanBeStoredInInventory() {
        return canBeStoredInInventory;
    }

    public AItemRestriction setCanBeStoredInInventory(boolean canBeStoredInInventory) {
        this.canBeStoredInInventory = canBeStoredInInventory;
        setChanged();

        return this;
    }

    public boolean isCanBeEquipped() {
        return canBeEquipped;
    }

    public AItemRestriction setCanBeEquipped(boolean canBeEquipped) {
        this.canBeEquipped = canBeEquipped;
        setChanged();

        return this;
    }

    public boolean isCanPickedUp() {
        return canPickedUp;
    }

    public AItemRestriction setCanPickedUp(boolean canPickedUp) {
        this.canPickedUp = canPickedUp;

        return this;
    }

    public boolean isHideTooltip() {
        return hideTooltip;
    }

    public AItemRestriction setHideTooltip(boolean hideTooltip) {
        this.hideTooltip = hideTooltip;

        return this;
    }

    public boolean isRenderItemName() {
        return renderItemName;
    }

    public AItemRestriction setRenderItemName(boolean renderItemName) {
        this.renderItemName = renderItemName;

        return this;
    }

    public boolean isHideInJEI() {
        return hideInJEI;
    }

    public AItemRestriction setHideInJEI(boolean hideInJEI) {
        this.hideInJEI = hideInJEI;

        return this;
    }

    public boolean isCanBePlaced() {
        return canBePlaced;
    }

    public AItemRestriction setCanBePlaced(boolean canBePlaced) {
        this.canBePlaced = canBePlaced;

        return this;
    }

//    public boolean isCanBlockBeUsed() {
//        return canBlockBeUsed;
//    }
//
//    public AItemRestriction setCanBlockBeUsed(boolean canBlockBeUsed) {
//        this.canBlockBeUsed = canBlockBeUsed;
//
//        return this;
//    }

    public boolean isCanItemBeUsed() {
        return canItemBeUsed;
    }

    public AItemRestriction setCanItemBeUsed(boolean canItemBeUsed) {
        this.canItemBeUsed = canItemBeUsed;

        return this;
    }

    public boolean isCanBeDig() {
        return canBeDig;
    }

    public AItemRestriction setCanBeDig(boolean canBeDig) {
        this.canBeDig = canBeDig;

        return this;
    }

    public Component getHiddenName(ItemStack stack) {
        return hiddenName.apply(stack);
    }

    public AItemRestriction setHiddenName(Function<ItemStack, Component> hiddenName) {
        this.hiddenName = hiddenName;

        return this;
    }

    public Component getDropMessage(ItemStack stack) {
        return dropMessage.apply(stack);
    }

    public AItemRestriction setDropMessage(Function<ItemStack, Component> dropMessage) {
        this.dropMessage = dropMessage;

        return this;
    }

    public Component getAttackMessage(ItemStack stack) {
        return attackMessage.apply(stack);
    }

    public AItemRestriction setAttackMessage(Function<ItemStack, Component> attackMessage) {
        this.attackMessage = attackMessage;

        return this;
    }

    public Component getPickupMessage(ItemStack stack) {
        return pickupMessage.apply(stack);
    }

    public AItemRestriction setPickupMessage(Function<ItemStack, Component> pickupMessage) {
        this.pickupMessage = pickupMessage;

        return this;
    }

    public Component getUsageMessage(ItemStack stack) {
        return usageMessage.apply(stack);
    }

    public AItemRestriction setUsageMessage(Function<ItemStack, Component> usageMessage) {
        this.usageMessage = usageMessage;

        return this;
    }

    public Component getMineMessage(ItemStack stack) {
        return mineMessage.apply(stack);
    }

    public AItemRestriction setMineMessage(Function<ItemStack, Component> mineMessage) {
        this.mineMessage = mineMessage;

        return this;
    }

    public Component getPlaceMessage(ItemStack stack) {
        return placeMessage.apply(stack);
    }

    public AItemRestriction setPlaceMessage(Function<ItemStack, Component> placeMessage) {
        this.placeMessage = placeMessage;

        return this;
    }

    public Component getJadeItemMessage(ItemStack stack) {
        return jadeItemMessage.apply(stack);
    }

    public AItemRestriction setJadeItemMessage(Function<ItemStack, Component> jadeItemMessage) {
        this.jadeItemMessage = jadeItemMessage;

        return this;
    }

    public Component getJadeBlockMessage(ItemStack stack) {
        return jadeBlockMessage.apply(stack);
    }

    public AItemRestriction setJadeBlockMessage(Function<ItemStack, Component> jadeBlockMessage) {
        this.jadeBlockMessage = jadeBlockMessage;

        return this;
    }
}
