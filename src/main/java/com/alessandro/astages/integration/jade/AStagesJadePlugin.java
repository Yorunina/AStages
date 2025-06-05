package com.alessandro.astages.integration.jade;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@WailaPlugin
public class AStagesJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(AStagesBlockComponentProvider.INSTANCE, BlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        if (!Mods.JADE.isLoaded()) { return; }
        registration.registerBlockComponent(AStagesBlockComponentProvider.INSTANCE, Block.class);

        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                var original = blockAccessor.getBlockState();
                var restriction = AClientRestrictionManager.ORE_INSTANCE.getRestriction(original);

                if (restriction != null) {
                    return registration.blockAccessor().from(blockAccessor).blockState(restriction.getReplacement()).build();
                }
            }

            return accessor;
        });

        registration.addTooltipCollectedCallback((tooltip, accessor) -> {
            if (accessor instanceof EntityAccessor entityAccessor) {
                var entity = entityAccessor.getEntity();
                var type = entity.getType();

                var restriction = AClientRestrictionManager.MOB_INSTANCE.getRestriction(type);

                if (restriction != null) {
                    tooltip.clear();

                    if (!restriction.isValueNull(Attributes.Mob.JADE_MOB_MESSAGE)) {
                        tooltip.add(restriction.get(Attributes.Mob.JADE_MOB_MESSAGE).get());
                    }
                }
            }

            if (accessor instanceof BlockAccessor blockAccessor) {
                var original = blockAccessor.getBlock();
                var stack = AStagesUtil.blockToStack(original);
                var restriction = AClientRestrictionManager.ITEM_INSTANCE.getProperties(stack);

                if (restriction != null) {
                    tooltip.clear();

                    if (restriction.jadeBlockMessage() != null) {
                        tooltip.add(restriction.jadeBlockMessage());
                    }
                }
            }

            if (accessor instanceof EntityAccessor entityAccessor) {
                var original = entityAccessor.getEntity();

                if (original instanceof ItemEntity itemEntity) {
                    var restriction = AClientRestrictionManager.ITEM_INSTANCE.getProperties(itemEntity.getItem());

                    if (restriction != null) {
                        tooltip.clear();

                        if (restriction.jadeItemMessage() != null) {
                            tooltip.add(restriction.jadeItemMessage());
                        }
                    }
                }
            }
        });
    }
}
