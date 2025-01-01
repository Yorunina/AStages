package com.alessandro.astages.test.factory;

import com.alessandro.astages.core.AItemRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AItemFactory {
    public static AItemRestriction getRestriction(EntityItemPickupEvent event) {
        if (canBeRunForPlayer(event.getEntity())) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), event.getItem().getItem());

            if (restriction != null && !restriction.canPickedUp) {
                event.setCanceled(true);
                event.getItem().setPickUpDelay(restriction.pickUpDelay);

                if (restriction.pickupMessage != null) {
                    event.getEntity().displayClientMessage(restriction.getPickupMessage(event.getItem().getItem()), true);
                }
            }
        }

        return null;
    }

    private static boolean canBeRunForPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}
