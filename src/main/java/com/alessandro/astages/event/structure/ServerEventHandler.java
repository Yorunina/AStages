package com.alessandro.astages.event.structure;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.AStructureRestriction;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.ToBeTested;
import com.alessandro.astages.util.Triple;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    public static final Map<UUID, Triple<Boolean, AStructureRestriction, ResourceLocation>> playerIsInStructure = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (ARestrictionManager.STRUCTURE_INSTANCE.getRestrictions().isEmpty()) { return; }
        if (event.player.level().isClientSide) { return; }
        if (event.phase == TickEvent.Phase.START) { return; }
        if (event.player.getServer() == null) { return; }

        if (event.player instanceof ServerPlayer player) {
            StructureManager manager = Objects.requireNonNull(player.getServer().getLevel(player.level().dimension())).structureManager();
            UUID playerUUID = player.getUUID();

            ARestrictionManager.STRUCTURE_INSTANCE.getRestrictions().forEach((stage, restrictions) -> {
                if (!AStagesUtil.hasStage(player, stage)) {
                    for (var restriction : restrictions) {
                        for (var structureResource : restriction.structures) {
                            Structure structure = manager.registryAccess().registryOrThrow(Registries.STRUCTURE).get(structureResource);

                            if (structure != null) {
                                boolean isInStructure = manager.getStructureAt(player.getOnPos(), structure).isValid();

                                if (isInStructure && !playerIsInStructure.getOrDefault(playerUUID, new Triple<>(false, null, null)).a()) {
                                    // playerIsInStructure.put(playerUUID, true);
                                    playerIsInStructure.put(playerUUID, new Triple<>(true, restriction, structureResource));
                                } else if (!isInStructure && playerIsInStructure.getOrDefault(playerUUID, new Triple<>(false, null, null)).a()) {
                                    // playerIsInStructure.put(playerUUID, false);
                                    playerIsInStructure.put(playerUUID, new Triple<>(false, null, null));
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (canBeRunForPlayer(event.getPlayer())) {
            for (UUID uuid : playerIsInStructure.keySet()) {
                var isInStructure = playerIsInStructure.get(uuid).a();
                var restriction = playerIsInStructure.get(uuid).b();
                var structure = playerIsInStructure.get(uuid).c();

                if (event.getPlayer().getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(event.getPlayer(), restriction.stage)) {
                    if (!restriction.canBlockBeBroken) {
                        event.setCanceled(true);
//                        event.setResult(Event.Result.DENY);

                        if (restriction.breakMessage != null) {
                            event.getPlayer().displayClientMessage(restriction.getBreakMessage(structure), true);
                        }
                    }
                }
            }
        }
    }

//    @ToBeTested
//    @SubscribeEvent
//    public static void onItemUsed(PlayerInteractEvent.RightClickItem event) {
//        if (canBeRunForPlayer(event.getEntity())) {
//            for (UUID uuid : playerIsInStructure.keySet()) {
//                var isInStructure = playerIsInStructure.get(uuid).a();
//                var restriction = playerIsInStructure.get(uuid).b();
//                var structure = playerIsInStructure.get(uuid).c();
//
//                if (event.getEntity().getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(event.getEntity(), restriction.stage)) {
//                    if (!restriction.canInteract) {
//                        event.setCanceled(true);
////                        event.setResult(Event.Result.DENY);
//
//                        if (restriction.interactMessage != null) {
//                            event.getEntity().displayClientMessage(restriction.getInteractMessage(structure), true);
//                        }
//                    }
//                }
//            }
//        }
//    }

    @ToBeTested
    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.RightClickBlock event) {
        if (canBeRunForPlayer(event.getEntity())) {
            for (UUID uuid : playerIsInStructure.keySet()) {
                var isInStructure = playerIsInStructure.get(uuid).a();
                var restriction = playerIsInStructure.get(uuid).b();
                var structure = playerIsInStructure.get(uuid).c();

                if (event.getEntity().getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(event.getEntity(), restriction.stage)) {
                    if (!restriction.canInteract) {
                        event.setCanceled(true);

                        if (restriction.interactMessage != null) {
                            event.getEntity().displayClientMessage(restriction.getInteractMessage(structure), true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(AttackEntityEvent event) {
        if (canBeRunForPlayer(event.getEntity())) {
            for (UUID uuid : playerIsInStructure.keySet()) {
                var isInStructure = playerIsInStructure.get(uuid).a();
                var restriction = playerIsInStructure.get(uuid).b();
                var structure = playerIsInStructure.get(uuid).c();

                if (event.getEntity().getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(event.getEntity(), restriction.stage)) {
                    if (!restriction.canAttackEntities) {
                        event.setCanceled(true);

                        if (restriction.attackMessage != null) {
                            event.getEntity().displayClientMessage(restriction.getAttackMessage(structure), true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (canBeRunForPlayer(player)) {
                for (UUID uuid : playerIsInStructure.keySet()) {
                    var isInStructure = playerIsInStructure.get(uuid).a();
                    var restriction = playerIsInStructure.get(uuid).b();
                    var structure = playerIsInStructure.get(uuid).c();

                    if (event.getEntity().getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(player, restriction.stage)) {
                        if (!restriction.canBlockBePlaced) {
                            event.setCanceled(true);
                            // Synchronize changes with client!
                            var slot = player.getInventory().selected;
                            player.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, slot, player.getInventory().getItem(slot)));

                            if (restriction.placeMessage != null) {
                                player.displayClientMessage(restriction.getPlaceMessage(structure), true);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonation(ExplosionEvent.Detonate event) {
        var player = AStagesUtil.getNearestPlayer(event.getLevel(), event.getExplosion().getPosition());
        if (canBeRunForPlayer(player)) {
            for (UUID uuid : playerIsInStructure.keySet()) {
                var isInStructure = playerIsInStructure.get(uuid).a();
                var restriction = playerIsInStructure.get(uuid).b();
                var structure = playerIsInStructure.get(uuid).c();

                if (player.getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(player, restriction.stage)) {
                    if (!restriction.makeExplosionsAffectBlocks) {
                        event.getAffectedBlocks().clear();
                    }

                    if (!restriction.makeExplosionsAffectEntities) {
                        event.getAffectedEntities().clear();
                    }
                }
            }
        }
    }

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
