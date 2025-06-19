package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.Attributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.event.CurioEquipEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class CuriosEventHandler {
    @SubscribeEvent
    public static void onCurioEquip(CurioEquipEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var curio = event.getStack();
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, curio);

            AStages.LOGGER.debug(curio.toString());

            if (restriction != null && restriction.isDisabled(Attributes.CURIO_EQUIPPING)) {
                event.setResult(Event.Result.DENY);
                restriction.displayMessage(Attributes.Item.CURIOS_MESSAGE, curio, player);
            }
        }
    }
}
