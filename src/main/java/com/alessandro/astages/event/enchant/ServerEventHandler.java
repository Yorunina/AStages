package com.alessandro.astages.event.enchant;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.EnchantWrapper;
import com.alessandro.astages.event.CommonEventSettings;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@NotNullParamsAndMethodsReturn
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!CommonEventSettings.requireSlotCheck()) { return; }

        if (event.phase == TickEvent.Phase.START && event.player != null && !event.player.level().isClientSide && !(event.player instanceof FakePlayer)) {
            Player player = event.player;
            Inventory inventory = player.getInventory();

            if (CommonEventSettings.getSlotChanged() == null) {
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack slotContent = inventory.getItem(i);

                    if (slotContent.getItem() instanceof EnchantedBookItem) {
                        var parsedBook = removeAllRestrictedEnchantmentFromEnchantedBook(player, slotContent);

                        if (!parsedBook.isEmpty()) {
                            inventory.setItem(CommonEventSettings.getSlotChanged(), parsedBook);
                        }
                    } else if (slotContent.isEnchanted()) {
                        removeAllRestrictedEnchantmentFromStack(player, slotContent);
                    }
                }
            } else {
                ItemStack slotContent = inventory.getItem(CommonEventSettings.getSlotChanged());

                if (slotContent.getItem() instanceof EnchantedBookItem) {
                    var parsedBook = removeAllRestrictedEnchantmentFromEnchantedBook(player, slotContent);

                    if (!parsedBook.isEmpty()) {
                        inventory.setItem(CommonEventSettings.getSlotChanged(), parsedBook);
                    }
                } else if (slotContent.isEnchanted()) {
                    removeAllRestrictedEnchantmentFromStack(player, slotContent);
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

    public static boolean stackHasBannedEnchantments(Player player, ItemStack stack) {
        if (stack.getItem() instanceof EnchantedBookItem) {
            var tag = EnchantedBookItem.getEnchantments(stack); // Where enchantments are stored!

            for (int i = 0; i < tag.size(); i++) {
                var compound = tag.getCompound(i);
                var enchantment = ForgeRegistries.ENCHANTMENTS.getValue(EnchantmentHelper.getEnchantmentId(compound));
                var level = EnchantmentHelper.getEnchantmentLevel(compound);

                if (enchantment != null) {
                    var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new EnchantWrapper(enchantment, level));

                    if (restriction != null && restriction.isDisabled(Attributes.ANVIL)) {
                        return true;
                    }
                }
            }
        } else {
            if (!stack.isEnchanted()) { return false; }

            var allEnchantments = stack.getAllEnchantments();

            for (var enchantment : allEnchantments.keySet()) {
                var level = allEnchantments.get(enchantment);
                var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new EnchantWrapper(enchantment, level));

                if (restriction != null && restriction.isDisabled(Attributes.ANVIL)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static ItemStack removeAllRestrictedEnchantmentFromEnchantedBook(Player player, ItemStack enchantedBook) {
        if (enchantedBook.isEmpty()) { return ItemStack.EMPTY; }

        var enchantments = EnchantmentHelper.getEnchantments(enchantedBook);

        var atLeastOneRemoved = false;
        var iterator = enchantments.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new EnchantWrapper(entry.getKey(), entry.getValue()));

            if (restriction != null && restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
                iterator.remove();
                atLeastOneRemoved = true;
            }
        }

        if (atLeastOneRemoved) {
            var newStack = enchantedBook.copy();

            newStack.removeTagKey("StoredEnchantments");
            enchantments.forEach(newStack::enchant);

            return newStack;
        }

        return ItemStack.EMPTY;
    }

    public static void removeAllRestrictedEnchantmentFromStack(Player player, ItemStack itemStack) {
        if (itemStack.isEmpty()) { return; }

        var enchantments = EnchantmentHelper.getEnchantments(itemStack);

        var atLeastOneRemoved = false;
        var iterator = enchantments.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new EnchantWrapper(entry.getKey(), entry.getValue()));

            if (restriction != null && restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
                iterator.remove();
                atLeastOneRemoved = true;
            }
        }

        if (atLeastOneRemoved) {
            EnchantmentHelper.setEnchantments(enchantments, itemStack);
        }
    }
}
