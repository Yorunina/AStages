package com.alessandro.astages.event.ore;

import com.alessandro.astages.AStages;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
//    @SubscribeEvent
//    public static void onModelBakeChange(ModelEvent.ModifyBakingResult event) {
//        for (Map.Entry<String, List<AClientOreRestriction>> entry : AClientRestrictionManager.ORE_INSTANCE.restrictions.entrySet()) {
//            for (AClientOreRestriction restriction : entry.getValue()) {
//                event.getModels(restriction.original().getBlockHolder())
//                restriction.original(), new AOreBakedModel(entry.getKey(), restriction.original(), restriction.replacement())
//            }
//        }
//    }
}
