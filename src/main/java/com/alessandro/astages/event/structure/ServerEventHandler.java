package com.alessandro.astages.event.structure;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.Info;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    public static final Map<UUID, Boolean> playerIsInStructure = new HashMap<>();

    @Info("To be tested!")
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

                                if (isInStructure && !playerIsInStructure.getOrDefault(playerUUID, false)) {
                                    playerIsInStructure.put(playerUUID, true);
                                } else if (!isInStructure && playerIsInStructure.getOrDefault(playerUUID, false)) {
                                    playerIsInStructure.put(playerUUID, false);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @Info("Player can't break block in the structure")
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (canBeRunForPlayer(event.getPlayer())) {
            for (UUID uuid : playerIsInStructure.keySet()) {
                if (event.getPlayer().getUUID().equals(uuid) && playerIsInStructure.get(uuid)) {
                    event.setCanceled(true);
                    event.setResult(Event.Result.DENY);
                    AStages.LOGGER.debug("Can't break this structure!");
                }
            }
        }
    }

    @Info("To be tested!")
    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent event) {
        if (canBeRunForPlayer(event.getEntity())) {
            for (UUID uuid : playerIsInStructure.keySet()) {
                if (event.getEntity().getUUID().equals(uuid) && playerIsInStructure.get(uuid)) {
                    event.setCanceled(true);
                    event.setResult(Event.Result.DENY);
                    AStages.LOGGER.debug("Can't interact with this structure!");
                }
            }
        }
    }

    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        for (UUID uuid : playerIsInStructure.keySet()) {
            if (event.getEntity().getUUID().equals(uuid) && playerIsInStructure.get(uuid)) {
                event.setNewSpeed(-1);
                AStages.LOGGER.debug("Can't destroy whi block!");
            }
        }
    }

    public static void explosion(ExplosionEvent event) {
//        if (!event.getLevel().isClientSide) {
//            for (UUID uuid : playerIsInStructure.keySet()) {
//                if (event..getUUID().equals(uuid) &&playerIsInStructure.get(uuid)){
//
//                }
//            }
//        }
    }

    public static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
