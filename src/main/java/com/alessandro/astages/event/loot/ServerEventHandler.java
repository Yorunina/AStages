package com.alessandro.astages.event.loot;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        var iterator = event.getDrops().iterator();
        var source = event.getSource().getEntity();
        var entity = event.getEntity();
        var server = entity.getServer();
        var toBeAdded = new ArrayList<ItemEntity>();
        Player player;

        if (source instanceof Player p) {
            player = p;
        } else {
            player = AStagesUtil.getNearestPlayer(entity.level(), entity.blockPosition());
        }

        while (iterator.hasNext()) {
            var drop = iterator.next();
            var stack = drop.getItem();

            var restriction = ARestrictionManager.LOOT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), stack, entity.getType(), null);

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
