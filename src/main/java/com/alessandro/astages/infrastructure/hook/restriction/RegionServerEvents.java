package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.APlayerUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class RegionServerEvents {
    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        var pos = new BlockPos((int) event.getX(), (int) event.getY(), (int) event.getZ());
        var vec = new Vec3((int) event.getX(), (int) event.getY(), (int) event.getZ());

        var player = APlayerUtils.getNearestPlayer(event.getLevel().getLevel(), vec);

        if (canBeRunForPlayer(player)) {
            var restriction = ARestrictionManager.REGION_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), pos);

            if (restriction != null && restriction.isDisabled(Attributes.MOB_SPAWNING)) {
                var restrictedDimension = restriction.get(Attributes.DIMENSION);

                if (restrictedDimension == null || event.getLevel().getLevel().dimension().location().equals(restrictedDimension)) {
                    event.setSpawnCancelled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onGenericInteraction(PlayerInteractEvent event) {
        if (event.isCancelable() && !event.getLevel().isClientSide && event.getEntity() instanceof ServerPlayer serverPlayer) {
            var restriction = ARestrictionManager.REGION_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), event.getPos());

            if (restriction != null && restriction.isDisabled(Attributes.GENERIC_INTERACTIONS)) {
                var restrictedDimension = restriction.get(Attributes.DIMENSION);

                if (restrictedDimension == null || event.getLevel().dimension().location().equals(restrictedDimension)) {
                    event.setCanceled(true);
                    serverPlayer.displayClientMessage(restriction.get(Attributes.Region.INTERACT_MESSAGE).get(), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonation(ExplosionEvent.Detonate event) {
        var player = APlayerUtils.getNearestPlayer(event.getLevel(), event.getExplosion().getPosition());

        if (canBeRunForPlayer(player)) {
            var rawPos = event.getExplosion().getPosition();
            var pos = new BlockPos((int) rawPos.x, (int) rawPos.y, (int) rawPos.z);
            var restriction = ARestrictionManager.REGION_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), pos);

            if (restriction != null) {
                var restrictedDimension = restriction.get(Attributes.DIMENSION);

                if (restrictedDimension == null || event.getLevel().dimension().location().equals(restrictedDimension)) {
                    if (restriction.isDisabled(Attributes.EXPLOSIONS_AFFECT_BLOCKS)) {
                        event.getAffectedBlocks().clear();
                    }

                    if (restriction.isDisabled(Attributes.EXPLOSIONS_AFFECT_ENTITIES)) {
                        event.getAffectedEntities().clear();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCommandExecuted(CommandEvent event) {
        var serverPlayer = event.getParseResults().getContext().getSource().getPlayer();
        var stringCommand = event.getParseResults().getReader().getString();

        if (canBeRunForPlayer(serverPlayer)) {
            var restriction = ARestrictionManager.REGION_INSTANCE.getRestriction(AHolder.serverAndPlayer(serverPlayer), serverPlayer.getOnPos());

            if (restriction != null && restriction.isDisabled(Attributes.PERFORM_COMMANDS)) {
                var foundedCommands = restriction.getDisabledCommands().stream().filter(stringCommand::contains).count();

                if (foundedCommands != 0) {
                    event.setCanceled(true);
                    restriction.displayMessage(Attributes.Region.COMMAND_MESSAGE, stringCommand, serverPlayer);
                }
            }
        }
    }

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
