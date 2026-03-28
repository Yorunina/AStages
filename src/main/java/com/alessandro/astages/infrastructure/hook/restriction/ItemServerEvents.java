package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.util.AInventoryUtils;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.ABlockStateUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.item.ABaseItemRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.hook.CommonEventSettings;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ItemServerEvents {
    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        if (canBeRunForPlayer(event.getEntity())) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(event.getEntity()), event.getItem().getItem());

            if (restriction != null && restriction.isDisabled(Attributes.PICKING_UP)) {
                event.setCanceled(true);

                event.getItem().setPickUpDelay(restriction.get(Attributes.PICK_UP_DELAY));
                restriction.displayMessage(Attributes.Item.PICKING_UP_MESSAGE, event.getItem().getItem(), event.getEntity());
            }
        }
    }

    @Info("Try to use PlayerEvent.BreakSpeed event!")
    @SubscribeEvent
    public static void breakSpeed(BlockEvent.BreakEvent event) {
        boolean isClientSide = event.getPlayer().level().isClientSide;
        if (isClientSide) { return; }

        var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(event.getPlayer()), ABlockStateUtils.stateToStack(event.getState()));
        if (restriction != null && restriction.isDisabled(Attributes.BLOCK_BREAKING)) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);

            restriction.displayMessage(Attributes.Item.MINING_MESSAGE, ABlockStateUtils.stateToStack(event.getState()), event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), event.getItemStack());

            if (restriction != null && restriction.isDisabled(Attributes.RIGHT_CLICK_INTERACTIONS)) {
                event.setCanceled(true);
                restriction.displayMessage(Attributes.Item.USING_MESSAGE, event.getItemStack(), event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), event.getItemStack());

            if (restriction != null && restriction.isDisabled(Attributes.RIGHT_CLICK_INTERACTIONS)) {
                event.setCanceled(true);
                if (event.getEntity() instanceof ServerPlayer player) {
                    AInventoryUtils.updateSelectedSlot(player);
                }
                restriction.displayMessage(Attributes.Item.USING_MESSAGE, event.getItemStack(), event.getEntity());
            }
//            else if (restriction != null && restriction.isEnabled(Attributes.IGNORE_BLOCKS_AROUND) && restriction.isEnabled(Attributes.BLOCK_PLACING)) {
//                return;
//            }
            else if (restriction == null) {
                var block = ABlockStateUtils.stateToStack(event.getLevel().getBlockState(event.getPos()));
                restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), block);

                if (restriction != null && restriction.isDisabled(Attributes.BLOCK_INTERACTIONS)) {
                    event.setCanceled(true);
                    if (event.getEntity() instanceof ServerPlayer player) {
                        AInventoryUtils.updateSelectedSlot(player);
                    }
                    restriction.displayMessage(Attributes.Item.USING_MESSAGE, block, event.getEntity());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.LeftClickBlock event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), event.getItemStack());

            if (restriction != null && restriction.isDisabled(Attributes.LEFT_CLICK_INTERACTIONS)) {
                event.setCanceled(true);
                restriction.displayMessage(Attributes.Item.USING_MESSAGE, event.getItemStack(), event.getEntity());
            }
//            else if (restriction == null) {
//                var block = AStagesUtil.stateToStack(event.getLevel().getBlockState(event.getPos()));
//                restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(block);
//
//                if (restriction != null && restriction.isDisabled(Attributes.LEFT_CLICK_INTERACTIONS)) {
//                    event.setCanceled(true);
//
//                    restriction.displayMessage(Attributes.Item.USING_MESSAGE, block, event.getEntity());
//                }
//            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), event.getItemStack());

            if (restriction != null && (restriction.isDisabled(Attributes.LEFT_CLICK_INTERACTIONS) || restriction.isDisabled(Attributes.RIGHT_CLICK_INTERACTIONS))) {
                event.setCanceled(true);
                restriction.displayMessage(Attributes.Item.USING_MESSAGE, event.getItemStack(), event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), event.getItemStack());

            if (restriction != null && (restriction.isDisabled(Attributes.RIGHT_CLICK_INTERACTIONS) || restriction.isDisabled(Attributes.RIGHT_CLICK_INTERACTIONS))) {
                event.setCanceled(true);
                restriction.displayMessage(Attributes.Item.USING_MESSAGE, event.getItemStack(), event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && !event.getLevel().isClientSide()) {
            var stack = new ItemStack(event.getPlacedBlock().getBlock());
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), stack);

            if (restriction != null && restriction.isDisabled(Attributes.BLOCK_PLACING)) {
                event.setCanceled(true);
                AInventoryUtils.updateSelectedSlot(player);
                restriction.displayMessage(Attributes.Item.PLACING_MESSAGE, stack, player);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(AttackEntityEvent event) {
        if (canBeRunForPlayer(event.getEntity())) {
            var player = event.getEntity();
            ItemStack stack = player.getMainHandItem();
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), stack);

            if (restriction != null && restriction.isDisabled(Attributes.ATTACKING)) {
                event.setCanceled(true);

                restriction.displayMessage(Attributes.Item.ATTACK_MESSAGE, stack, player);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTickContainer(TickEvent.PlayerTickEvent event) {
        if (!CommonEventSettings.requireContainerCheck()) { return; }

        if (event.phase == TickEvent.Phase.START && event.player != null && !event.player.level().isClientSide && !(event.player instanceof FakePlayer)) {
            boolean isAnotherInventoryOpened = CommonEventSettings.hasPlayerAnotherContainerOpened(event.player);

            if (isAnotherInventoryOpened) {
                var container = CommonEventSettings.getContainerOpenedByPlayer(event.player);

                for (var slot : container.slots) {
                    if (slot.container == event.player.getInventory()) {
                        var restriction = ARestrictionManager.ITEM_INSTANCE.getInventoryRestriction(AHolder.serverAndPlayer(event.player), slot.getItem());

                        if (restriction != null && restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
                            event.player.drop(slot.getItem(), false);
                            container.setItem(slot.index, container.getStateId(), ItemStack.EMPTY);
                        }
                    } else {
                        var restriction = ARestrictionManager.ITEM_INSTANCE.getContainerRestriction(AHolder.serverAndPlayer(event.player), slot.getItem(), slot);

                        if (restriction != null && restriction.isDisabled(Attributes.STORING_IN_CONTAINERS)) {
                            event.player.drop(slot.getItem(), false);
                            container.setItem(slot.index, container.getStateId(), ItemStack.EMPTY);
                        }
                    }
                }
            }

            CommonEventSettings.resetContainerChanged();
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!CommonEventSettings.requireSlotCheck()) { return; }

        if (event.phase == TickEvent.Phase.START && event.player != null && !event.player.level().isClientSide && !(event.player instanceof FakePlayer)) {
            Player player = event.player;

            boolean isAnotherInventoryOpened = CommonEventSettings.hasPlayerAnotherContainerOpened(event.player);
            if (isAnotherInventoryOpened) { return; } // Delegate actions to method above!

            Inventory inventory = player.getInventory();

            final int armorStart = inventory.items.size();
            final int armorEnd = armorStart + inventory.armor.size();

            if (CommonEventSettings.getSlotChanged() == null) { // Check whole inventory
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack slotContent = inventory.getItem(i);

                    if (!slotContent.isEmpty()) {
                        ABaseItemRestriction<?, ?> restriction;

                        if (i >= armorStart && i <= armorEnd) {
                            restriction = ARestrictionManager.ITEM_INSTANCE.getEquipmentRestriction(AHolder.serverAndPlayer(player), slotContent);
                        } else {
                            restriction = ARestrictionManager.ITEM_INSTANCE.getInventoryRestriction(AHolder.serverAndPlayer(player), slotContent);
                        }

                        if (restriction != null) {
                            restriction.displayMessage(Attributes.Item.DROP_MESSAGE, slotContent, player);

                            inventory.setItem(i, ItemStack.EMPTY);
                            player.drop(slotContent, false);
                        }
                    }
                }
            } else { // Check single slot
                ItemStack slotContent = inventory.getItem(CommonEventSettings.getSlotChanged());

                if (!slotContent.isEmpty()) {
                    ABaseItemRestriction<?, ?> restriction;

                    if (CommonEventSettings.getSlotChanged() >= armorStart && CommonEventSettings.getSlotChanged() <= armorEnd) {
                        restriction = ARestrictionManager.ITEM_INSTANCE.getEquipmentRestriction(AHolder.serverAndPlayer(player), slotContent);
                    } else {
                        restriction = ARestrictionManager.ITEM_INSTANCE.getInventoryRestriction(AHolder.serverAndPlayer(player), slotContent);
                    }

                    if (restriction != null) {
                        restriction.displayMessage(Attributes.Item.DROP_MESSAGE, slotContent, player);

                        inventory.setItem(CommonEventSettings.getSlotChanged(), ItemStack.EMPTY);
                        player.drop(slotContent, false);
                    }
                }
            }

            CommonEventSettings.resetSlotChanged();
        }
    }

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
