package com.alessandro.astages.event.mob;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.AMobRestriction;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.develop.ToBeTested;
import com.alessandro.astages.util.develop.UnderDevelopment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void checkMobSpawning(MobSpawnEvent.PositionCheck event) {
        Player nearestPlayer = AStagesUtil.getNearestPlayer(event.getLevel().getLevel(), new Vec3(event.getX(), event.getY(), event.getZ()));
        var server = event.getEntity().getServer();
        var level = event.getEntity().level();
        var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(event.getEntity().getType(), nearestPlayer, server);

        if (restriction != null) {
            if (restriction.isDisabled(Attributes.MOB_SPAWNING)) {
                preventSpawning(event, restriction, level);
                return;
            }

            if (restriction.getDisabledSpawnTypes().contains(event.getSpawnType())) {
                preventSpawning(event, restriction, level);
                return;
            }

            if (!restriction.isValueNull(Attributes.DIMENSION)) {
                if (restriction.get(Attributes.DIMENSION).equals(level.dimension().location())) {
                    preventSpawning(event, restriction, level);
                    return;
                }
            }

            var biome = level.getBiome(event.getEntity().blockPosition()).getKey();
            if (biome != null) {
                var biomeRS = biome.location();
                if (restriction.getRestrictedBiomes().contains(biomeRS)) {
                    preventSpawning(event, restriction, level);
                    return;
                }
            }

            var lightLevel = level.getLightEmission(event.getEntity().blockPosition());
            if (!restriction.isValueNull(Attributes.MIN_LIGHT_LEVEL) && !restriction.isValueNull(Attributes.MAX_LIGHT_LEVEL)) {
                if (restriction.get(Attributes.MIN_LIGHT_LEVEL) < lightLevel && lightLevel < restriction.get(Attributes.MAX_LIGHT_LEVEL)) {
                    preventSpawning(event, restriction, level);
//                     return;
                }
            } else if (!restriction.isValueNull(Attributes.MIN_LIGHT_LEVEL) && restriction.isValueNull(Attributes.MAX_LIGHT_LEVEL)) {
                if (restriction.get(Attributes.MIN_LIGHT_LEVEL) < lightLevel) {
                    preventSpawning(event, restriction, level);
//                     return;
                }
            } else if (restriction.isValueNull(Attributes.MIN_LIGHT_LEVEL) && !restriction.isValueNull(Attributes.MAX_LIGHT_LEVEL)) {
                if (lightLevel < restriction.get(Attributes.MAX_LIGHT_LEVEL)) {
                    preventSpawning(event, restriction, level);
//                     return;
                }
            }
        }
    }

    private static void preventSpawning(MobSpawnEvent.PositionCheck event, AMobRestriction restriction, Level level) {
        // If prevent spawn, you can place the replacer!
        if (!restriction.isValueNull(Attributes.REPLACE)) {
            LivingEntity newEntity = (LivingEntity) restriction.get(Attributes.REPLACE).create(level);

            if (newEntity != null) {
                if (restriction.isEnabled(Attributes.SPAWN_WITH_DIFFERENT_EQUIPMENT)) {
                    for (var wrapper : restriction.getEquipments()) {
                        newEntity.setItemSlot(wrapper.slot(), wrapper.stack());
                    }
                }

                newEntity.setPos(event.getX(), event.getY(), event.getZ());
                level.addFreshEntity(newEntity);
            } else {
                AStages.LOGGER.warn("Features disabled in this level to spawn the replacer for restriction with id {}!", restriction.getId());
            }
        }

        event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
    }

    @ToBeTested
    @UnderDevelopment
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        var player = event.getEntity();
        var server = player.getServer();
        var entityType = event.getTarget().getType();

        var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(entityType, player, server);

        if (restriction != null && restriction.isDisabled(Attributes.RIGHT_CLICK_INTERACTIONS)) {
            event.setCanceled(true);
            player.displayClientMessage(restriction.get(Attributes.Mob.INTERACTION_MESSAGE).get(), true);

            //event.setCancellationResult(InteractionResult.PASS);
        }
    }

    @ToBeTested
    @UnderDevelopment
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerAttack(AttackEntityEvent event) {
        var player = event.getEntity();
        var server = player.getServer();
        var entityType = event.getTarget().getType();

        var restriction = ARestrictionManager.MOB_INSTANCE.getRestriction(entityType, player, server);

        if (restriction != null && restriction.isDisabled(Attributes.ATTACKING)) {
            event.setCanceled(true);
            player.displayClientMessage(restriction.get(Attributes.Mob.ATTACK_MESSAGE).get(), true);
        }
    }
}
