package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.item.AItemRestriction;
import com.alessandro.astages.event.CommonEventSettings;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.develop.Info;
import com.alessandro.astages.util.develop.UnderDevelopment;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        if (canBeRunForPlayer(event.getEntity())) {
            var restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(event.getEntity(), event.getItem().getItem());

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

        var restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(event.getPlayer(), AStagesUtil.stateToStack(event.getState()));
        if (restriction != null && restriction.isDisabled(Attributes.BLOCK_BREAKING)) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);

            restriction.displayMessage(Attributes.Item.MINING_MESSAGE, AStagesUtil.stateToStack(event.getState()), event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());

            if (restriction != null && restriction.isDisabled(Attributes.RIGHT_CLICK_INTERACTIONS)) {
                event.setCanceled(true);
                restriction.displayMessage(Attributes.Item.USING_MESSAGE, event.getItemStack(), event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());

            if (restriction != null && restriction.isDisabled(Attributes.RIGHT_CLICK_INTERACTIONS)) {
                event.setCanceled(true);
                restriction.displayMessage(Attributes.Item.USING_MESSAGE, event.getItemStack(), event.getEntity());
            }
//            else if (restriction != null && restriction.isEnabled(Attributes.IGNORE_BLOCKS_AROUND) && restriction.isEnabled(Attributes.BLOCK_PLACING)) {
//                return;
//            }
            else if (restriction == null) {
                var block = AStagesUtil.stateToStack(event.getLevel().getBlockState(event.getPos()));
                restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(serverPlayer, block);

                if (restriction != null && restriction.isDisabled(Attributes.BLOCK_INTERACTIONS)) {
                    event.setCanceled(true);
                    restriction.displayMessage(Attributes.Item.USING_MESSAGE, block, event.getEntity());
                }
            }

            if (event.getEntity() instanceof ServerPlayer player) {
                var slot = player.getInventory().selected;
                player.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, slot, player.getInventory().getItem(slot)));
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.LeftClickBlock event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());

            if (restriction != null && restriction.isDisabled(Attributes.LEFT_CLICK_INTERACTIONS)) {
                event.setCanceled(true);
                restriction.displayMessage(Attributes.Item.USING_MESSAGE, event.getItemStack(), event.getEntity());
            }
//            else if (restriction == null) {
//                var block = AStagesUtil.stateToStack(event.getLevel().getBlockState(event.getPos()));
//                restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(serverPlayer, block);
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
            var restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());

            if (restriction != null && (restriction.isDisabled(Attributes.LEFT_CLICK_INTERACTIONS) || restriction.isDisabled(Attributes.RIGHT_CLICK_INTERACTIONS))) {
                event.setCanceled(true);
                restriction.displayMessage(Attributes.Item.USING_MESSAGE, event.getItemStack(), event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());

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
            var restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(player, stack);

            if (restriction != null && restriction.isDisabled(Attributes.BLOCK_PLACING)) {
                event.setCanceled(true);
                // Synchronize changes with client!
                var slot = player.getInventory().selected;
                player.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, slot, player.getInventory().getItem(slot)));

                restriction.displayMessage(Attributes.Item.PLACING_MESSAGE, stack, player);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(AttackEntityEvent event) {
        if (canBeRunForPlayer(event.getEntity())) {
            var player = event.getEntity();
            ItemStack stack = player.getMainHandItem();
            var restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getRestriction(player, stack);

            if (restriction != null && restriction.isDisabled(Attributes.ATTACKING)) {
                event.setCanceled(true);

                restriction.displayMessage(Attributes.Item.ATTACK_MESSAGE, stack, player);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerOpenContainer(PlayerContainerEvent.Open event) {
        CommonEventSettings.playersHaveOtherInventoriesOpened.put(event.getEntity().getUUID(), true);
    }

    @SubscribeEvent
    public static void onPlayerCloseContainer(PlayerContainerEvent.Close event) {
        CommonEventSettings.playersHaveOtherInventoriesOpened.put(event.getEntity().getUUID(), false);
    }

    @Info("To be RE-IMPLEMENTED!")
    @UnderDevelopment
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!CommonEventSettings.isInventoryChanged) { return; }

        if (event.phase == TickEvent.Phase.START && event.player != null && !event.player.level().isClientSide && !(event.player instanceof FakePlayer)) {
            Player player = event.player;
            Inventory inventory = player.getInventory();

            final int armorStart = inventory.items.size();
            final int armorEnd = armorStart + inventory.armor.size();

            if (CommonEventSettings.slotChanged == null || CommonEventSettings.playersHaveOtherInventoriesOpened.getOrDefault(event.player.getUUID(), false)) {
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack slotContent = inventory.getItem(i);

                    if (!slotContent.isEmpty()) {
                        AItemRestriction restriction;

                        if (i >= armorStart && i <= armorEnd) {
//                            restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getEquipmentRestriction(event.player, slotContent);
                        } else {
//                            restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getInventoryRestriction(event.player, slotContent);
                        }

//                        if (restriction != null) {
//                            restriction.displayMessage(Attributes.Item.DROP_MESSAGE, slotContent, player);
//
//                            inventory.setItem(i, ItemStack.EMPTY);
//                            player.drop(slotContent, false);
//                        }
                    }
                }
            } else {
                ItemStack slotContent = inventory.getItem(CommonEventSettings.slotChanged);

                if (!slotContent.isEmpty()) {
                    AItemRestriction restriction;

                    if (CommonEventSettings.slotChanged >= armorStart && CommonEventSettings.slotChanged <= armorEnd) {
//                        restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getEquipmentRestriction(event.player, slotContent);
                    } else {
//                        restriction = ARestrictionManager.NEW_ITEM_INSTANCE.getInventoryRestriction(event.player, slotContent);
                    }

//                    if (restriction != null) {
//                        restriction.displayMessage(Attributes.Item.DROP_MESSAGE, slotContent, player);
//
//                        inventory.setItem(CommonEventSettings.slotChanged, ItemStack.EMPTY);
//                        player.drop(slotContent, false);
//
//                        AStages.LOGGER.debug(inventory.getItem(CommonEventSettings.slotChanged).toString());
//                    }
                }
            }

            CommonEventSettings.isInventoryChanged = false;
        }
    }

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
