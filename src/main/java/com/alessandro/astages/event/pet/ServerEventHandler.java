package com.alessandro.astages.event.pet;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
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

            var restriction = ARestrictionManager.PET_INSTANCE.getRestriction(pet.getType(), player, player.getServer());

            if (restriction != null && restriction.isDisabled(Attributes.TAMABLE)) {
                event.setCanceled(true);

                restriction.displayMessage(Attributes.Pet.TAME_MESSAGE, pet, player);
            }
        }
    }

    @SubscribeEvent
    public static void onMount(EntityMountEvent event) {
        if (!event.getEntityMounting().level().isClientSide) {
            var entity = event.getEntityMounting();
            var pet = event.getEntityBeingMounted();

            if (entity instanceof Player player) {
                var restriction = ARestrictionManager.PET_INSTANCE.getRestriction(pet.getType(), player, player.getServer());

                if (restriction != null && restriction.isDisabled(Attributes.MOUNTABLE)) {
                    event.setCanceled(true);

                    restriction.displayMessage(Attributes.Pet.MOUNT_MESSAGE, pet, player);
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

            var restriction = ARestrictionManager.PET_INSTANCE.getRestriction(pet.getType(), player, player.getServer());

            if (restriction != null && restriction.isDisabled(Attributes.BREEDABLE) && !item.isEmpty()) {
                event.setCanceled(true);

                restriction.displayMessage(Attributes.Pet.BREED_MESSAGE, pet, player);
            }
        }
    }
}
