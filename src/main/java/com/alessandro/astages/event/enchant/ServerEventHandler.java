package com.alessandro.astages.event.enchant;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.EnchantWrapper;
import com.alessandro.astages.event.CommonEventSettings;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.develop.ToBeTested;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!CommonEventSettings.requireSlotCheck()) { return; }

        if (!event.getEntity().level().isClientSide && !(event.getEntity() instanceof FakePlayer)) {
            Player player = event.getEntity();
            Inventory inventory = player.getInventory();

            if (CommonEventSettings.getSlotChanged() == null) {
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack slotContent = inventory.getItem(i);

                    if (!slotContent.isEnchanted() || slotContent.getItem() instanceof EnchantedBookItem) {
                        var stack = removeAllRestrictedEnchantmentFromStack(player, slotContent);
                        inventory.setItem(i, stack);
                    }
                }
            } else {
                ItemStack slotContent = inventory.getItem(CommonEventSettings.getSlotChanged());

                if (!slotContent.isEnchanted() || slotContent.getItem() instanceof EnchantedBookItem) {
                    var stack = removeAllRestrictedEnchantmentFromStack(player, slotContent);
                    inventory.setItem(CommonEventSettings.getSlotChanged(), stack);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.getRight() == ItemStack.EMPTY || event.getLeft() == ItemStack.EMPTY) {
            return;
        }

        var player = event.getPlayer();
        var isClientSide = player.level().isClientSide;
        if (isClientSide) { return; }

        if (stackHasBannedEnchantments(player, event.getLeft())) {
            event.setCanceled(true);
            return;
        }

        if (stackHasBannedEnchantments(player, event.getRight())) {
            event.setCanceled(true);
        }
    }

    @ToBeTested
    public static boolean stackHasBannedEnchantments(Player player, ItemStack stack) {
        if (stack.getItem() instanceof EnchantedBookItem) {
            if (stack.has(DataComponents.STORED_ENCHANTMENTS)) {
                var data = stack.get(DataComponents.STORED_ENCHANTMENTS); // Where enchantments are stored!
                if (data == null) { return false; }
                var enchantments = data.entrySet();

                for (var holder : enchantments) {
                    var enchantment = holder.getKey().value();
                    var level = holder.getIntValue();

                    var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(player, new EnchantWrapper(enchantment, level));

                    if (restriction != null && restriction.isDisabled(Attributes.ANVIL)) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        } else {
            if (stack.has(DataComponents.ENCHANTMENTS)) {
                var data = stack.get(DataComponents.ENCHANTMENTS); // Where enchantments are stored!
                if (data == null) { return false; }
                var enchantments = data.entrySet();

                for (var holder : enchantments) {
                    var enchantment = holder.getKey().value();
                    var level = holder.getIntValue();

                    var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(player, new EnchantWrapper(enchantment, level));

                    if (restriction != null && restriction.isDisabled(Attributes.ANVIL)) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        }

        return false;
    }

    public static @NotNull ItemStack removeAllRestrictedEnchantmentFromStack(Player player, ItemStack itemStack) {
        ItemStack newStack = itemStack.copy();
//        newStack.remove(DataComponents.ENCHANTMENTS);
//        newStack.remove(DataComponents.STORED_ENCHANTMENTS);
        clearStack(newStack);

        if (itemStack.has(DataComponents.ENCHANTMENTS)) {
            var data = itemStack.get(DataComponents.ENCHANTMENTS); // Where enchantments are stored!
            if (data == null) { return newStack; }
            var enchantments = data.entrySet();

            for (var holder : enchantments) {
                var enchantment = holder.getKey();
                var level = holder.getIntValue();

                var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(player, new EnchantWrapper(enchantment.value(), level));

                if (restriction == null || restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
                    newStack.enchant(enchantment, level);
                }
            }
        }

        if (itemStack.has(DataComponents.STORED_ENCHANTMENTS)) {
            var data = itemStack.get(DataComponents.STORED_ENCHANTMENTS); // Where enchantments are stored!
            if (data == null) { return newStack; }
            var enchantments = data.entrySet();

            for (var holder : enchantments) {
                var enchantment = holder.getKey();
                var level = holder.getIntValue();

                var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(player, new EnchantWrapper(enchantment.value(), level));

                if (restriction == null || restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
                    newStack.enchant(enchantment, level);
                }
            }
        }

        return newStack;
    }

    public static ItemStack clearStack(ItemStack stack) {
        if (stack.has(DataComponents.ENCHANTMENTS)) {
            ItemEnchantments data = stack.get(DataComponents.ENCHANTMENTS);
            if (data != null) {
                stack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            }
        }

        if (stack.has(DataComponents.STORED_ENCHANTMENTS)) {
            ItemEnchantments data = stack.get(DataComponents.STORED_ENCHANTMENTS);
            if (data != null) {
                stack.set(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
            }
        }

        return stack;
    }
}
