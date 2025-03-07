package com.alessandro.astages.event;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
//@EventBusSubscriber(modid = AStages.MODID)
public class BlockEventHandler {
//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public static void onAttachedCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
//        event.addCapability(new ResourceLocation(AStages.MODID, "owners"), new BlockStageProvider());
//    }
//
//    @SubscribeEvent
//    public static void blockPlaced(BlockEvent.EntityPlaceEvent event) {
//        if (event.getEntity() instanceof Player player) {
//            BlockPos pos = event.getPos();
//
//            if (event.getLevel().getBlockEntity(pos) != null) {
//                Objects.requireNonNull(event.getLevel().getBlockEntity(pos)).getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
//                    blockStage.setOwner(player.getUUID());
//                });
//            }
//        }
//    }
}
