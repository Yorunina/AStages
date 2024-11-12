package com.alessandro.astages.integration.jade;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.integration.Mods;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
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
                var restriction = ARestrictionManager.ORE_INSTANCE.getRestriction(original);

                if (restriction != null) {
                    return registration.blockAccessor().from(blockAccessor).blockState(restriction.replacement).build();
                }
            }

            return accessor;
        });

        registration.addTooltipCollectedCallback((tooltip, accessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                var original = blockAccessor.getBlock();
                var stack = new ItemStack(original);
                var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(stack);

                if (restriction != null) {
                    tooltip.clear();

                    if (restriction.jadeBlockMessage != null) {
                        tooltip.add(restriction.getJadeBlockMessage(stack));
                    }
                }
            }

            if (accessor instanceof EntityAccessor entityAccessor) {
                var original = entityAccessor.getEntity();

                if (original instanceof ItemEntity itemEntity) {
                    var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(itemEntity.getItem());

                    if (restriction != null) {
                        tooltip.clear();

                        if (restriction.jadeItemMessage != null) {
                            tooltip.add(restriction.getJadeItemMessage(itemEntity.getItem()));
                        }
                    }
                }
            }
        });
    }
}
