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
        Player nearestPlayer = AStagesUtil.getNearestPlayer(event.getLevel().getLevel(), new Vec3(event.getX(), event.getY(), event.getZ()));
        var server = event.getEntity().getServer();
        var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(event.getEntity().getType(), nearestPlayer, server);

        if (restriction != null && restriction.isDisabled(Attributes.MOB_SPAWNING)) {
            if (event.getSpawnType() == MobSpawnType.SPAWNER && restriction.isDisabled(Attributes.SPAWNER)) {
                event.setResult(Event.Result.DENY);
                return;
            }

            var dimension = restriction.get(Attributes.DIMENSION);
            if (restriction.get(Attributes.DIMENSION) != null) {
                if (event.getEntity().level().dimension().location() == dimension) {
                    event.setResult(Event.Result.DENY);
                    return;
                }
            }

            if (restriction.get(Attributes.REPLACE) != null) {
                var level = event.getLevel().getLevel();

                Entity newEntity = Objects.requireNonNull(restriction.get(Attributes.REPLACE).create(level));
                newEntity.setPos(event.getX(), event.getY(), event.getZ());
                level.addFreshEntity(newEntity);

                event.setResult(Event.Result.DENY);
                return;
            }

            event.setResult(Event.Result.DENY);
        }
    }
}
