package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @Info("For exp settings! - For other ore changing see mixin/ore package!")
    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(event.getPlayer(), event.getState());

        if (restriction != null) {
            var stack = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);

            int fortuneLevel = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
            int silkTouchLevel = stack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
            event.setExpToDrop(restriction.getReplacement().getExpDrop(event.getLevel(), event.getLevel().getRandom(), event.getPos(), fortuneLevel, silkTouchLevel));
        }
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
