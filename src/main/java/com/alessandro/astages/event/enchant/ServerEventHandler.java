package com.alessandro.astages.event.enchant;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AEnchantManager;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.CommonEventSettings;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!CommonEventSettings.isInventoryChanged) { return; }

        if (event.phase == TickEvent.Phase.START && event.player != null && !event.player.level().isClientSide && !(event.player instanceof FakePlayer)) {
            Player player = event.player;
            Inventory inventory = player.getInventory();

            if (CommonEventSettings.slotChanged == null) {
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack slotContent = inventory.getItem(i);

                    if (!slotContent.isEnchanted() || slotContent.getItem() instanceof EnchantedBookItem) {
                        var stack = removeAllRestrictedEnchantmentFromStack(player, slotContent);
                        inventory.setItem(i, stack);
                    }
                }
            } else {
                ItemStack slotContent = inventory.getItem(CommonEventSettings.slotChanged);

                if (!slotContent.isEnchanted() || slotContent.getItem() instanceof EnchantedBookItem) {
                    var stack = removeAllRestrictedEnchantmentFromStack(player, slotContent);
                    inventory.setItem(CommonEventSettings.slotChanged, stack);
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
                    var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(player, new AEnchantManager.EnchantWrapper(enchantment, level));

                    if (restriction != null && restriction.isAnvilRestricted) {
                        return true;
                    }
                }
            }
        } else {
            if (!stack.isEnchanted()) { return false; }

            var allEnchantments = stack.getAllEnchantments();

            for (var enchantment : allEnchantments.keySet()) {
                var level = allEnchantments.get(enchantment);
                var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(player, new AEnchantManager.EnchantWrapper(enchantment, level));

                if (restriction != null && restriction.isAnvilRestricted) {
                    return true;
                }
            }
        }

        return false;
    }

    public static @NotNull ItemStack removeAllRestrictedEnchantmentFromStack(Player player, ItemStack itemStack) {
        ItemStack newStack = itemStack.copy();
        newStack.removeTagKey("Enchantments"); // Items
        newStack.removeTagKey("StoredEnchantments"); // Books

        EnchantmentHelper.getEnchantments(itemStack).forEach(((enchantment, level) -> {
            var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(player, new AEnchantManager.EnchantWrapper(enchantment, level));

            if (restriction == null || !restriction.isInventoryRestricted) {
                newStack.enchant(enchantment, level);
            }
        }));

        return newStack;
    }
}
