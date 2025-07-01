package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class CuriosEventHandler {
//    @SubscribeEvent
//    public static void onCurioEquip(CurioEquipEvent event) {
//        if (event.getEntity() instanceof ServerPlayer player) {
//            var curio = event.getStack();
//            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, curio);
//
//            if (restriction != null && restriction.isDisabled(Attributes.CURIO_EQUIPPING)) {
//                event.setResult(Event.Result.DENY);
//                restriction.displayMessage(Attributes.Item.CURIOS_MESSAGE, curio, player);
//            }
//        }
//    }
}
