package com.alessandro.astages.event.pet;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void onPlayerTame(AnimalTameEvent event) {
        if (!event.getTamer().level().isClientSide) {
            var player = event.getTamer();
            var pet = event.getEntity();

            var restriction = ARestrictionManager.PET_INSTANCE.getRestriction(player, pet.getType());

            if (restriction != null && !restriction.isTamable) {
                event.setCanceled(true);
                player.displayClientMessage(restriction.getTameMessage(pet), true);
            }
        }
    }

    @SubscribeEvent
    public static void onMount(EntityMountEvent event) {
        if (!event.getEntityMounting().level().isClientSide) {
            var entity = event.getEntityMounting();
            var pet = event.getEntityBeingMounted();

            if (entity instanceof Player player) {
                var restriction = ARestrictionManager.PET_INSTANCE.getRestriction(player, pet.getType());

                if (restriction != null && !restriction.isMountable) {
                    event.setCanceled(true);
                    player.displayClientMessage(restriction.getMountMessage(pet), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerBreedEntity(PlayerInteractEvent.EntityInteract event) {
        if (!event.getEntity().level().isClientSide) {
            var player = event.getEntity();
            var pet = event.getTarget();
            var item = event.getEntity().getItemInHand(event.getHand());

            var restriction = ARestrictionManager.PET_INSTANCE.getRestriction(player, pet.getType());

            if (restriction != null && !restriction.isBreedable && !item.isEmpty()) {
                event.setCanceled(true);
                player.displayClientMessage(restriction.getBreedMessage(pet), true);
            }
        }
    }
}
