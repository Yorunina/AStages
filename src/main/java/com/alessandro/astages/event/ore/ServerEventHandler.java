package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @Info("For exp settings! - For other ore changing see mixin/ore package!")
    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (canBeRunForPlayer(event.getPlayer())) {
            var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(event.getPlayer(), event.getState());

            if (restriction != null) {
                var stack = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);

                int fortuneLevel = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
                int silkTouchLevel = stack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
                event.setExpToDrop(restriction.getReplacement().getExpDrop(event.getLevel(), event.getLevel().getRandom(), event.getPos(), fortuneLevel, silkTouchLevel));
            }
        }
    }

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
