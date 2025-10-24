package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.capability.BlockStageProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class BlockEventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttachedCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        event.addCapability(AResourceLocation.fromNamespaceAndPath("owners"), new BlockStageProvider());
    }

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
}
