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
                    var newValue = EnchantmentHelper.processBlockExperience(event.getLevel(), event.getTool(), restriction.getReplacement().getExpDrop(event.getLevel(), event.getPos(), event.getBlockEntity(), player, event.getTool()));
                    event.setDroppedExperience(newValue);
                }
            }
        }
    }
    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
