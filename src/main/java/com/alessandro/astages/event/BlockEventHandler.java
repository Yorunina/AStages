package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.BlockStageProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class BlockEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttachedCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        event.addCapability(new ResourceLocation(AStages.MODID, "owners"), new BlockStageProvider());
    }

//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public static void onAttachedCap(AttachCapabilitiesEvent<MBDMachine> event) {
//        event.addCapability(new ResourceLocation(AStages.MODID, "owners"), new BlockStageProvider());
//    }

    @SubscribeEvent
    public static void blockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            BlockPos pos = event.getPos();

            if (event.getLevel().getBlockEntity(pos) != null) {
                Objects.requireNonNull(event.getLevel().getBlockEntity(pos)).getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
                    blockStage.setOwner(player.getUUID());
                });
            }
        }
    }

//    @SubscribeEvent
//    public static void machinePlaced(MachinePlacedEvent event) {
//        AStages.LOGGER.debug("Fired Machine Place!");
//
//        event.getPlayer().level().getBlockEntity(event.getMachine().getPos()).getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
//            AStages.LOGGER.debug("Set!");
//            blockStage.setOwner(event.getPlayer().getUUID());
//        });

//        AStages.LOGGER.debug(event.getMachine().getCapability(BlockStageProvider.BLOCK_STAGE).resolve().toString());
//
//
//
//        var tag = new CompoundTag();
//        tag.putString("astages:owners", event.getPlayer().getUUID().toString());
//        AStages.LOGGER.debug(event.getMachine().getCustomData().toString());
//        event.getMachine().getCustomData().getCompound("ForgeCaps").putUUID("astages:owners", event.getPlayer().getUUID());
//    }
}
