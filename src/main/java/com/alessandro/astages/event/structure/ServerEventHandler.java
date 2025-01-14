package com.alessandro.astages.event.structure;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.AStructureRestriction;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.base.Triple;
import com.alessandro.astages.util.develop.ToBeTested;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    public static final Map<UUID, Triple<Boolean, AStructureRestriction, ResourceLocation>> playerIsInStructure = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (ARestrictionManager.STRUCTURE_INSTANCE.getRestrictions().isEmpty()) { return; }
        if (event.getEntity().level().isClientSide) { return; }
        if (event.getEntity().getServer() == null) { return; }

        if (event.getEntity() instanceof ServerPlayer player) {
            StructureManager manager = Objects.requireNonNull(player.getServer().getLevel(player.level().dimension())).structureManager();
            UUID playerUUID = player.getUUID();

            ARestrictionManager.STRUCTURE_INSTANCE.getRestrictions().forEach((stage, restrictions) -> {
                if (!AStagesUtil.hasStage(player, stage)) {
                    for (var restriction : restrictions) {
                        for (var structureResource : restriction.getStructures()) {
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

                if (event.getPlayer().getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(event.getPlayer(), restriction.getStage())) {
                    if (restriction.isDisabled(Attributes.BLOCK_BREAKING)) {
                        event.setCanceled(true);
//                        event.setResult(Event.Result.DENY);

                        restriction.displayMessage(Attributes.Structure.MINING_MESSAGE, structure, event.getPlayer());
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

                if (event.getEntity().getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(event.getEntity(), restriction.getStage())) {
                    if (restriction.isDisabled(Attributes.GENERIC_INTERACTIONS)) {
                        event.setCanceled(true);

                        restriction.displayMessage(Attributes.Structure.INTERACT_MESSAGE, structure, event.getEntity());
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

                if (event.getEntity().getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(event.getEntity(), restriction.getStage())) {
                    if (restriction.isDisabled(Attributes.ATTACKING)) {
                        event.setCanceled(true);

                        restriction.displayMessage(Attributes.Structure.ATTACK_MESSAGE, structure, event.getEntity());
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

                    if (event.getEntity().getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(player, restriction.getStage())) {
                        if (restriction.isDisabled(Attributes.BLOCK_PLACING)) {
                            event.setCanceled(true);
                            // Synchronize changes with client!
                            var slot = player.getInventory().selected;
                            player.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, slot, player.getInventory().getItem(slot)));

                            restriction.displayMessage(Attributes.Structure.PLACING_MESSAGE, structure, player);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonation(ExplosionEvent.Detonate event) {
        var player = AStagesUtil.getNearestPlayer(event.getLevel(), event.getExplosion().center());
        if (canBeRunForPlayer(player)) {
            for (UUID uuid : playerIsInStructure.keySet()) {
                var isInStructure = playerIsInStructure.get(uuid).a();
                var restriction = playerIsInStructure.get(uuid).b();

                if (player.getUUID().equals(uuid) && isInStructure && !AStagesUtil.hasStage(player, restriction.getStage())) {
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

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
