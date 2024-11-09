package com.alessandro.astages.util;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.stage.AStageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class AStagesUtil {
    public static void showTitles(PlayerStage.Operation operation, String stage) {
        var aStage = AStageManager.getStage(stage);
        if (aStage != null) {
            setTimes(aStage.fadeIn, aStage.stay, aStage.fadeOut);

            if (operation == PlayerStage.Operation.ADD) {
                if (aStage.addTitle != null) {
                    setTitle(aStage.addTitle);
                }

                if (aStage.addSubTitle != null) {
                    setSubTitle(aStage.addSubTitle);
                }
            } else if (operation == PlayerStage.Operation.REMOVE) {
                if (aStage.removeTitle != null) {
                    setTitle(aStage.removeTitle);
                }

                if (aStage.removeSubTitle != null) {
                    AStagesUtil.setSubTitle(aStage.removeSubTitle);
                }
            }
        } else {
            if (AStagesCommon.ENABLE_TITLE_AFTER_STAGE_ADDING.get()) {
                setTimes(20, 60, 20);

                if (operation == PlayerStage.Operation.ADD) {
                    setTitle(Component.translatable("title.astages.add", stageToDescription(stage)).withStyle(AStagesCommon.TITLE_COLOR.get()));
                }
            }
        }
    }

    @Contract("_ -> !null")
    public static @NotNull String stageToDescription(@NotNull String input) {
        return capitalizeWords(input.replace('_', ' '));
    }

    public static @NotNull String capitalizeWords(@NotNull String input) {
        // split the input string into an array of words
        String[] words = input.split("\\s");

        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toTitleCase(word.charAt(0)))
                .append(word.substring(1))
                .append(" ");
        }

        return result.toString().trim();
    }

    public static void setTitle(Component component) {
        Minecraft.getInstance().gui.setTitle(component);
    }

    public static void setSubTitle(Component component) {
        Minecraft.getInstance().gui.setSubtitle(component);
    }

    public static void setTimes(int fadeIn, int stay, int fadeOut) {
        Minecraft.getInstance().gui.setTimes(fadeIn, stay, fadeOut);
    }

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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasStage(@NotNull Player player, String stage) {
        AtomicBoolean toReturn = new AtomicBoolean(false);

        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> toReturn.set(playerStage.getStages().contains(stage)));

        return toReturn.get();
    }

    public static Player getPlayerFromUUID(@NotNull MinecraftServer server, UUID uuid) {
        return server.getPlayerList().getPlayer(uuid);
    }

    public static @NotNull BakedModel getBakedModelFromState(BlockState state) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
    }

    public static void setBakedModelForState(BlockState state, BakedModel bakedModel) {
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
}
