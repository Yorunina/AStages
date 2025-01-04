package com.alessandro.astages.event.mob;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void checkMobSpawning(MobSpawnEvent.PositionCheck event) {

//        if (event.getSpawnType() == MobSpawnType.SPAWNER)
//            return;
//        event.ge
        Player nearestPlayer = AStagesUtil.getNearestPlayer(event.getLevel().getLevel(), new Vec3(event.getX(), event.getY(), event.getZ()));
//        Player nearestPlayer = event.getLevel().getNearestPlayer(a -> (), event.getEntity());
        var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(nearestPlayer, event.getEntity().getType());

        if (restriction != null) {
            if (event.getSpawnType() == MobSpawnType.SPAWNER && restriction.isDisabled(Attributes.SPAWNER)) {
                event.setResult(Event.Result.DENY);
                return;
            }

            if (restriction.getAttribute(Attributes.DIMENSION) != null) {
                event.setResult(Event.Result.DENY);
                return;
            }

            if (restriction.getAttribute(Attributes.REPLACE) != null) {
                var level = event.getLevel().getLevel();

                Entity newEntity = Objects.requireNonNull(restriction.getAttribute(Attributes.REPLACE).create(level));
                newEntity.setPos(event.getX(), event.getY(), event.getZ());
                level.addFreshEntity(newEntity);

                event.setResult(Event.Result.DENY);
                return;
            }

            event.setResult(Event.Result.DENY);
        }
    }
}
