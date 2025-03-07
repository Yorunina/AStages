package com.alessandro.astages.event.mob;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void checkMobSpawning(MobSpawnEvent.PositionCheck event) {
        Player nearestPlayer = AStagesUtil.getNearestPlayer(event.getLevel().getLevel(), new Vec3(event.getX(), event.getY(), event.getZ()));
        var server = event.getEntity().getServer();
        var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(event.getEntity().getType(), nearestPlayer, server);

        if (restriction != null && restriction.isDisabled(Attributes.MOB_SPAWNING)) {
            if (event.getSpawnType() == MobSpawnType.SPAWNER && restriction.isDisabled(Attributes.SPAWNER)) {
                event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
                return;
            }

            var dimension = restriction.get(Attributes.DIMENSION);
            if (restriction.get(Attributes.DIMENSION) != null) {
                if (event.getEntity().level().dimension().location() == dimension) {
                    event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
                    return;
                }
            }

            if (restriction.get(Attributes.REPLACE) != null) {
                var level = event.getLevel().getLevel();

                Entity newEntity = Objects.requireNonNull(restriction.get(Attributes.REPLACE).create(level));
                newEntity.setPos(event.getX(), event.getY(), event.getZ());
                level.addFreshEntity(newEntity);

                event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
                return;
            }

            event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
        }
    }
}
