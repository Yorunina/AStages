package com.alessandro.astages.integration.jade;

import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum AStagesBlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final String OWNER_KEY = "owner";

    @Override
    public void appendTooltip(@NotNull ITooltip iTooltip, @NotNull BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains(OWNER_KEY)) {
            iTooltip.add(
                Component.literal("Owner: " + blockAccessor.getServerData().getString(OWNER_KEY))
            );
        }
    }

    @Contract(pure = true)
    @Override
    public @NotNull ResourceLocation getUid() {
        return new ResourceLocation("astages", "block_component_provider");
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, @NotNull BlockAccessor blockAccessor) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();

        blockEntity.getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
            if (blockAccessor.getPlayer().getServer() != null) {
                var player = AStagesUtil.getPlayerFromUUID(blockAccessor.getPlayer().getServer(), blockStage.getOwner());

                if (player != null) {
                    compoundTag.putString(OWNER_KEY, player.getName().getString());
                }
            }
        });
    }
}
