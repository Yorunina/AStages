package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AItemRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
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

        if (isClientSide) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AStagesUtil.stateToStack(event.getState()));
            if (restriction != null && !restriction.canBeDig) { event.setNewSpeed(-1f); }
            return;
        }

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
        if (event.getLevel().isClientSide && event.isCancelable()) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getItemStack());
            if (restriction != null && !restriction.canItemBeUsed) { event.setCanceled(true); }
            return;
        }

        if (event.isCancelable()) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), event.getItemStack());
            if (restriction != null && !restriction.canItemBeUsed) {
                event.setCanceled(true);

                if (restriction.usageMessage != null) {
                    event.getEntity().displayClientMessage(restriction.getUsageMessage(event.getItemStack()), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel().isClientSide()) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(new ItemStack(event.getPlacedBlock().getBlock()));

            if (restriction != null && !restriction.canItemBeUsed) {
                event.setCanceled(true);
            }

            return;
        }

        if (event.getEntity() instanceof ServerPlayer player) {
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
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
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
