package com.alessandro.astages.event.loot;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        var iterator = event.getDrops().iterator();
        var source = event.getSource().getEntity();
        var entity = event.getEntity();
        var toBeAdded = new ArrayList<ItemEntity>();
        Player player;

        if (source instanceof Player p) {
            player = p;
        } else {
            player = AStagesUtil.getNearestPlayer(entity.level(), entity.blockPosition());
        }

        if (player == null) { return; }

        while (iterator.hasNext()) {
            var drop = iterator.next();
            var stack = drop.getItem();

            var restriction = ARestrictionManager.LOOT_INSTANCE.getRestriction(player, stack, entity.getType(), null);

            if (restriction != null) {
                iterator.remove();

                if (restriction.isEnabled(Attributes.HAS_REPLACER)) {
                    var replacer = restriction.getReplacer().apply(stack);

                    if (!replacer.isEmpty()) {
                        var itemEntity = new ItemEntity(drop.level(), drop.getX(), drop.getY(), drop.getZ(), replacer);
                        toBeAdded.add(itemEntity);
                    }
                }
            }
        }

        event.getDrops().addAll(toBeAdded);
    }

    public static void onLootTableGenerated() {

    }
}