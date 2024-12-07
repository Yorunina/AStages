package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AItemRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
    public static boolean isInventoryChanged = false;

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        if (canBeRunForPlayer(event.getEntity())) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), event.getItem().getItem());

            if (restriction != null && !restriction.canPickedUp) {
                event.setCanceled(true);
                event.getItem().setPickUpDelay(restriction.pickUpDelay);

                if (restriction.pickupMessage != null) {
                    event.getEntity().displayClientMessage(restriction.getPickupMessage(event.getItem().getItem()), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        boolean isClientSide = event.getEntity().level().isClientSide;
        if (isClientSide) { return; }

//        if (isClientSide) {
//            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), AStagesUtil.stateToStack(event.getState()));
//            if (restriction != null && !restriction.canBeDig) { event.setNewSpeed(-1f); }
//            return;
//        }

        var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), AStagesUtil.stateToStack(event.getState()));
        if (restriction != null && !restriction.canBeDig) {
            event.setNewSpeed(-1f);

            if (restriction.mineMessage != null) {
                event.getEntity().displayClientMessage(restriction.getMineMessage(AStagesUtil.stateToStack(event.getState())), true);
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent event) {
//        if (event.getLevel().isClientSide && event.isCancelable()) {
//            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getItemStack());
//            if (restriction != null && !restriction.canItemBeUsed) { event.setCanceled(true); }
//            return;
//        }


//        event.setCancellationResult(InteractionResult.sidedSuccess(event.getSide().isClient()));

        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), event.getItemStack());
            if (restriction != null && !restriction.canItemBeUsed) {
//                event.getEntity().setItemInHand(InteractionHand.MAIN_HAND, event.getItemStack().copy());
//                event.getEntity().containerMenu.broadcastChanges();
//                event.getEntity().inventoryMenu.slotsChanged(event.getEntity().getInventory());
                // ClientboundContainerSetSlotPacket;
                // ((ServerPlayer) event.getEntity()).connection.send(new ClientboundContainerSetSlotPacket(event.getEntity().containerMenu.containerId, event.getEntity().containerMenu.getStateId(), event.getEntity().getSlot(1), ));
                // event.setCancellationResult();
//                event.setCancellationResult(InteractionResult.FAIL);
//                event.setResult(Event.Result.DENY);
                // event.setCancellationResult(InteractionResult.PASS);
                event.setCanceled(true);

//                var menu = serverPlayer.containerMenu;
//                var s = event.getItemStack().copy();
//                s.setCount(4);
//
//                var slot = serverPlayer.getInventory().findSlotMatchingItem(s);
//                AStages.LOGGER.debug(String.valueOf(slot));
//                serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.getStateId(), slot, s));

//                if (event.getEntity().getMainHandItem().equals(event.getItemStack(), false)) {
//                    event.getEntity().getMainHandItem().setCount(event.getEntity().getMainHandItem().getCount() + 1);
//                } else if (event.getEntity().getOffhandItem().equals(event.getItemStack(), false)) {
//                    event.getEntity().getOffhandItem().setCount(event.getEntity().getMainHandItem().getCount() + 1);
//                }

                // event.getEntity().containerMenu.setCarried(event.getItemStack().copy());
                // AStages.LOGGER.debug("PERFORMED!");
                // event.getItemStack().setCount(event.getItemStack().getCount());
                // event.getEntity().getInventory().setChanged();
                // event.getEntity().inventoryMenu.broadcastChanges();
                // event.getEntity().containerMenu.broadcastChanges();

                if (restriction.usageMessage != null) {
                    event.getEntity().displayClientMessage(restriction.getUsageMessage(event.getItemStack()), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && !event.getLevel().isClientSide()) {
            var stack = new ItemStack(event.getPlacedBlock().getBlock());
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, stack);

            if (restriction != null && !restriction.canBePlaced) {
                event.setCanceled(true);

                if (restriction.placeMessage != null) {
                    player.displayClientMessage(restriction.getPlaceMessage(stack), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(AttackEntityEvent event) {
        if (canBeRunForPlayer(event.getEntity())) {
            var player = event.getEntity();
            ItemStack stack = player.getMainHandItem();
            AItemRestriction restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, stack);

            if (restriction != null && !restriction.canAttack) {
                event.setCanceled(true);

                if (restriction.attackMessage != null) {
                    player.displayClientMessage(restriction.getAttackMessage(stack), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!isInventoryChanged) { return; }

        if (event.phase == TickEvent.Phase.START && event.player != null && !event.player.level().isClientSide && !(event.player instanceof FakePlayer)) {
            Player player = event.player;
            Inventory inventory = player.getInventory();

            final int armorStart = inventory.items.size();
            final int armorEnd = armorStart + inventory.armor.size();

            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack slotContent = inventory.getItem(i);

                if (!slotContent.isEmpty()) {
                    AItemRestriction restriction;

                    if (i >= armorStart && i <= armorEnd) {
                        restriction = ARestrictionManager.ITEM_INSTANCE.getEquipmentRestriction(event.player, slotContent);

                    } else {
                        restriction = ARestrictionManager.ITEM_INSTANCE.getInventoryRestriction(event.player, slotContent);
                        if (restriction != null) {
                            AStages.LOGGER.debug(restriction.toString());
                        }
                    }
                    if (restriction != null) {
                        if (restriction.dropMessage != null) {
                            player.displayClientMessage(restriction.getDropMessage(slotContent), true);
                        }

                        inventory.setItem(i, ItemStack.EMPTY);
                        player.drop(slotContent, false);
                    }
                }
            }

            isInventoryChanged = false;
        }
    }

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
