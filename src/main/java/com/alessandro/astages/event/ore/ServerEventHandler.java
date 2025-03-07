package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @Info("For exp settings! - For other ore changing see mixin/ore package!")
    @SubscribeEvent
    public static void onBlockBroken(BlockDropsEvent event) {
        if (event.getBreaker() instanceof Player player) {
            if (canBeRunForPlayer(player)) {
                var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(player, event.getState());

                if (restriction != null) {
//                    var stack = player.getItemInHand(InteractionHand.MAIN_HAND);

                    var newValue = EnchantmentHelper.processBlockExperience(event.getLevel(), event.getTool(), restriction.getReplacement().getExpDrop(event.getLevel(), event.getPos(), event.getBlockEntity(), player, event.getTool()));
                    event.setDroppedExperience(newValue);
//                    int fortuneLevel = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
//                    int silkTouchLevel = stack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
//                    event.setExpToDrop(restriction.getReplacement().getExpDrop(event.getLevel(), event.getLevel().getRandom(), event.getPos(), fortuneLevel, silkTouchLevel));
                }
            }
        }
    }
//        if (canBeRunForPlayer(event.getPlayer())) {
//            var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(event.getPlayer(), event.getState());
//
//            if (restriction != null) {
//                var stack = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
//
//                int fortuneLevel = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
//                int silkTouchLevel = stack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
//                event.setExpToDrop(restriction.getReplacement().getExpDrop(event.getLevel(), event.getLevel().getRandom(), event.getPos(), fortuneLevel, silkTouchLevel));
//            }
//        }
//    }
//
    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }

//    @SubscribeEvent
//    public static void setBreakSpeed(PlayerEvent.BreakSpeed event) {
//        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(event.getEntity(), event.getState());
//
//        if (restriction != null) {
//            event.setNewSpeed(restriction.getReplacement().getDestroySpeed(event.getEntity().level(), event.getPosition().orElse(new BlockPos(0, 0, 0))));
//        }
//
//        if (restriction != null) {
//            AStages.LOGGER.debug(restriction.getReplacement().toString());
//            // event.setNewSpeed(5000.0f);
//            event.setNewSpeed(event.getEntity().getDigSpeed(restriction.getReplacement(), event.getPosition().orElse(null)));
//            AStages.LOGGER.debug(String.valueOf(event.getOriginalSpeed()));
//            AStages.LOGGER.debug(String.valueOf(event.getEntity().getDigSpeed(restriction.getReplacement(), event.getPosition().orElse(null))));
//        }
//    }
}
