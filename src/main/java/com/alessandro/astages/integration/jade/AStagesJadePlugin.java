package com.alessandro.astages.integration.jade;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.integration.Mods;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.*;

@WailaPlugin
public class AStagesJadePlugin implements IWailaPlugin {
    @Override
    public void register(@NotNull IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(AStagesBlockComponentProvider.INSTANCE, BlockEntity.class);
    }

    @Override
    public void registerClient(@NotNull IWailaClientRegistration registration) {
        if (!Mods.JADE.isLoaded()) { return; }
        registration.registerBlockComponent(AStagesBlockComponentProvider.INSTANCE, Block.class);

        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                var original = blockAccessor.getBlockState();
                var restriction = AClientRestrictionManager.ORE_INSTANCE.getRestriction(original);

                if (restriction != null) {
                    return registration.blockAccessor().from(blockAccessor).blockState(restriction.replacement()).build();
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

                    if (restriction.jadeMobMessage() != null) {
                        tooltip.add(restriction.jadeMobMessage());
                    }
                }
            }

            if (accessor instanceof BlockAccessor blockAccessor) {
                var original = blockAccessor.getBlock();
                var stack = AStagesUtil.blockToStack(original);
                var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(stack);

                if (restriction != null) {
                    tooltip.clear();

                    if (restriction.jadeBlockMessage() != null) {
                        tooltip.add(restriction.jadeBlockMessage());
                    }
                }


//                var restriction = AClientRestrictionManager.NEW_ITEM_INSTANCE.getRestriction(stack);

                // TODO: To be re-implemented!
//                if (restriction != null) {
//                    tooltip.clear();
//
//                    if (restriction.jadeBlockMessage() != null) {
//                        tooltip.add(restriction.jadeBlockMessage());
//                    }
//                }
            }

            if (accessor instanceof EntityAccessor entityAccessor) {
                var original = entityAccessor.getEntity();

                if (original instanceof ItemEntity itemEntity) {
                    var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(itemEntity.getItem());

                    if (restriction != null) {
                        tooltip.clear();

                        if (restriction.jadeItemMessage() != null) {
                            tooltip.add(restriction.jadeItemMessage());
                        }
                    }

//                    var restriction = AClientRestrictionManager.NEW_ITEM_INSTANCE.getRestriction(itemEntity.getItem());

                    // TODO: To be re-implemented!
//                    if (restriction != null) {
//                        tooltip.clear();
//
//                        if (restriction.jadeItemMessage() != null) {
//                            tooltip.add(restriction.jadeItemMessage());
//                        }
//                    }
                }
            }
        });
    }
}
