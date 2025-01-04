package com.alessandro.astages.event.crop;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.ACropRestriction;
import com.alessandro.astages.core.wrapper.CropWrapper;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@Mod.EventBusSubscriber(modid = AStages.MODID)
@ParametersAreNonnullByDefault
public class ServerEventHandler {
    @SubscribeEvent
    public static void onCropGrowth(BlockEvent.CropGrowEvent.Post event) {
        if (!event.getLevel().isClientSide()) {
            var pos = event.getPos();
            var nearestPlayer = AStagesUtil.getNearestPlayer((Level) event.getLevel(), new Vec3(pos.getX(), pos.getY(), pos.getZ()));
            var level = event.getLevel();

            ACropRestriction restriction;
            if (event.getOriginalState().getBlock() instanceof CropBlock crop) {
                restriction = ARestrictionManager.CROP_INSTANCE.getRestriction(nearestPlayer, new CropWrapper(event.getOriginalState(), crop.getAge(event.getOriginalState())));
            } else {
                restriction = ARestrictionManager.CROP_INSTANCE.getRestriction(nearestPlayer, new CropWrapper(event.getOriginalState(), null));
            }

            if (restriction != null) {
                level.setBlock(pos, event.getOriginalState(), Block.UPDATE_ALL);
            }
        }
    }
}
