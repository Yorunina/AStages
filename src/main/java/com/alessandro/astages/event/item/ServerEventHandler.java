package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AItemRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.CommonEventSettings;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.Info;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
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
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
//    public static boolean isInventoryChanged = false;

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

    @Info("Try to use PlayerEvent.BreakSpeed event!")
    @SubscribeEvent
    public static void breakSpeed(BlockEvent.BreakEvent event) {
        boolean isClientSide = event.getPlayer().level().isClientSide;
        if (isClientSide) { return; }

        var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getPlayer(), AStagesUtil.stateToStack(event.getState()));
        if (restriction != null && !restriction.canBeDig) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);

            if (restriction.mineMessage != null) {
                event.getPlayer().displayClientMessage(restriction.getMineMessage(AStagesUtil.stateToStack(event.getState())), true);
            }
        }
    }

    @Info("Error for server!")
    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.RightClickEmpty event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());
            if (restriction != null && !restriction.canItemBeRightClicked) {
                event.setCanceled(true);

                if (restriction.usageMessage != null) {
                    event.getEntity().displayClientMessage(restriction.getUsageMessage(event.getItemStack()), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());
            if (restriction != null && !restriction.canItemBeRightClicked) {
                event.setCanceled(true);

                if (restriction.usageMessage != null) {
                    event.getEntity().displayClientMessage(restriction.getUsageMessage(event.getItemStack()), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.LeftClickBlock event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());
            if (restriction != null && !restriction.canItemBeLeftClicked) {
                event.setCanceled(true);

                if (restriction.usageMessage != null) {
                    event.getEntity().displayClientMessage(restriction.getUsageMessage(event.getItemStack()), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());
            if (restriction != null && !restriction.canItemBeLeftClicked) {
                event.setCanceled(true);

                if (restriction.usageMessage != null) {
                    event.getEntity().displayClientMessage(restriction.getUsageMessage(event.getItemStack()), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());
            if (restriction != null && !restriction.canItemBeRightClicked) {
                event.setCanceled(true);

                if (restriction.usageMessage != null) {
                    event.getEntity().displayClientMessage(restriction.getUsageMessage(event.getItemStack()), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(serverPlayer, event.getItemStack());
            if (restriction != null && !restriction.canItemBeRightClicked) {
                event.setCanceled(true);

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
                // Synchronize changes with client!
                var slot = player.getInventory().selected;
                player.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, slot, player.getInventory().getItem(slot)));


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
        if (!CommonEventSettings.isInventoryChanged) { return; }

        if (event.phase == TickEvent.Phase.START && event.player != null && !event.player.level().isClientSide && !(event.player instanceof FakePlayer)) {
            Player player = event.player;
            Inventory inventory = player.getInventory();

            final int armorStart = inventory.items.size();
            final int armorEnd = armorStart + inventory.armor.size();

            if (CommonEventSettings.slotChanged == null) {
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack slotContent = inventory.getItem(i);

                    if (!slotContent.isEmpty()) {
                        AItemRestriction restriction;

                        if (i >= armorStart && i <= armorEnd) {
                            restriction = ARestrictionManager.ITEM_INSTANCE.getEquipmentRestriction(event.player, slotContent);
                        } else {
                            restriction = ARestrictionManager.ITEM_INSTANCE.getInventoryRestriction(event.player, slotContent);
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
            } else {
                ItemStack slotContent = inventory.getItem(CommonEventSettings.slotChanged);

                if (!slotContent.isEmpty()) {
                    AItemRestriction restriction;

                    if (CommonEventSettings.slotChanged >= armorStart && CommonEventSettings.slotChanged <= armorEnd) {
                        restriction = ARestrictionManager.ITEM_INSTANCE.getEquipmentRestriction(event.player, slotContent);
                    } else {
                        restriction = ARestrictionManager.ITEM_INSTANCE.getInventoryRestriction(event.player, slotContent);
                    }

                    if (restriction != null) {
                        if (restriction.dropMessage != null) {
                            player.displayClientMessage(restriction.getDropMessage(slotContent), true);
                        }

                        inventory.setItem(CommonEventSettings.slotChanged, ItemStack.EMPTY);
                        player.drop(slotContent, false);
                    }
                }
            }

            CommonEventSettings.isInventoryChanged = false;
        }


//        if (!isInventoryChanged) { return; }
//
//        if (event.phase == TickEvent.Phase.START && event.player != null && !event.player.level().isClientSide && !(event.player instanceof FakePlayer)) {
//            Player player = event.player;
//            Inventory inventory = player.getInventory();
//
//            final int armorStart = inventory.items.size();
//            final int armorEnd = armorStart + inventory.armor.size();
//
//            for (int i = 0; i < inventory.getContainerSize(); i++) {
//                ItemStack slotContent = inventory.getItem(i);
//
//                if (!slotContent.isEmpty()) {
//                    AItemRestriction restriction;
//
//                    if (i >= armorStart && i <= armorEnd) {
//                        restriction = ARestrictionManager.ITEM_INSTANCE.getEquipmentRestriction(event.player, slotContent);
//                    } else {
//                        restriction = ARestrictionManager.ITEM_INSTANCE.getInventoryRestriction(event.player, slotContent);
//                        if (restriction != null) {
//                            AStages.LOGGER.debug(restriction.toString());
//                        }
//                    }
//
//                    if (restriction != null) {
//                        if (restriction.dropMessage != null) {
//                            player.displayClientMessage(restriction.getDropMessage(slotContent), true);
//                        }
//
//                        inventory.setItem(i, ItemStack.EMPTY);
//                        player.drop(slotContent, false);
//                    }
//                }
//            }
//
//            isInventoryChanged = false;
//        }
    }

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
