package com.alessandro.astages.util;

import com.alessandro.astages.capability.PlayerStageProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class AStagesUtil {
    // Util class

//    static {
//        AStagesUtil.runForSide(
//            true,
//            () -> AStages.LOGGER.debug("Client!"),
//            () -> AStages.LOGGER.debug("Server!")
//        );
//    }

    public static void runForSide(boolean discriminantForClient, Runnable client, Runnable server) {
        if (discriminantForClient) {
            client.run();
        } else {
            server.run();
        }
    }

    public static boolean isRealPlayer(Player player) {
        return player instanceof ServerPlayer;
    }

    public static boolean hasStage(@NotNull Player player, String stage) {
        return player.getData(PlayerStageProvider.PLAYER_STAGE).getStages().contains(stage);
    }

    public static Player getPlayerFromUUID(@NotNull MinecraftServer server, UUID uuid) {
        return server.getPlayerList().getPlayer(uuid);
    }

    public static @NotNull BakedModel getBakedModelFromState(BlockState state) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
    }

    public static void setBakedModelForState(BlockState state, BakedModel bakedModel) {
        // Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getModelManager().requiresRender();
        Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().modelByStateCache.put(state, bakedModel);
    }

    @Contract("_ -> new")
    public static @NotNull ItemStack stateToStack(@NotNull BlockState state) {
        return new ItemStack(state.getBlock());
    }

    @Contract("_ -> new")
    public static @NotNull ItemStack blockToStack(Block block) {
        return new ItemStack(block);
    }

    // Why not the default player implementation?!
    public static float getNewDigSpeed(@NotNull Player player, BlockState state) {
        float defaultSpeed = player.getInventory().getDestroySpeed(state);
        if (defaultSpeed > 1.0F) {
            defaultSpeed += (float) player.getAttributeValue(Attributes.MINING_EFFICIENCY);
        }

        if (MobEffectUtil.hasDigSpeed(player)) {
            defaultSpeed *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }

        if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float modifier = switch (Objects.requireNonNull(player.getEffect(MobEffects.DIG_SLOWDOWN)).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };

            defaultSpeed *= modifier;
        }

        defaultSpeed *= (float) player.getAttributeValue(Attributes.BLOCK_BREAK_SPEED);
        if (player.isEyeInFluid(FluidTags.WATER)) {
            defaultSpeed *= (float) Objects.requireNonNull(player.getAttribute(Attributes.SUBMERGED_MINING_SPEED)).getValue();
        }

        if (!player.onGround()) {
            defaultSpeed /= 5.0F;
        }

        return defaultSpeed;
    }
}
