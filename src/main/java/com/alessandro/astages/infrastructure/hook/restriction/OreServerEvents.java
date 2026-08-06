package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.util.EventGuards;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class OreServerEvents {
    @Info("For exp settings! - For other ore changing see mixin/ore package!")
    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (!EventGuards.isValidPlayer(event.getPlayer())) { return; }

        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(AHolder.serverAndPlayer(event.getPlayer()), event.getState());

        if (restriction != null) {
            var stack = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);

            int fortuneLevel = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
            int silkTouchLevel = stack.getEnchantmentLevel(Enchantments.SILK_TOUCH);
            event.setExpToDrop(restriction.getReplacement().getExpDrop(event.getLevel(), event.getLevel().getRandom(), event.getPos(), fortuneLevel, silkTouchLevel));
        }
    }

    @SubscribeEvent
    public static void onPlayerHarvest(PlayerEvent.HarvestCheck event) {
        var player = event.getEntity();
        if (!EventGuards.isValidPlayer(player)) { return; }

        var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), event.getTargetBlock());

        if (restriction != null) {
            event.setCanHarvest(player.hasCorrectToolForDrops(restriction.getReplacement()));
        }
    }
}
