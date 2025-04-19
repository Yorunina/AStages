package com.alessandro.astages.util;

import com.alessandro.astages.capability.AProvider;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.stage.AStageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AStagesUtil {
    public static Player getNearestPlayer(@NotNull Level level, @NotNull BlockPos pos) {
        return getNearestPlayer(level, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static Player getNearestPlayer(@NotNull Level level, Vec3 pos) {
        var players = level.players();
        var minDistance = Double.MAX_VALUE;
        Player toReturn = null;

        for (Player player : players) {
            var distance = player.distanceToSqr(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
            if (distance < minDistance) {
                minDistance = distance;
                toReturn = player;
            }
        }

        return toReturn;
    }

    public static void showTitles(ServerPlayer player, PlayerStage.Operation operation, String stage) {
        Component title = Component.empty();
        Component subtitle = Component.empty();
        var fadeIn = 0;
        var fadeOut = 0;
        var stay = 0;

        var aStage = AStageManager.getStage(stage);
        if (aStage != null) {
            fadeIn = aStage.getFadeIn();
            stay = aStage.getStay();
            fadeOut = aStage.getFadeOut();

            if (operation == PlayerStage.Operation.ADD) {
                if (aStage.getAddTitle() != null) {
                    title = aStage.getAddTitle();
                }

                if (aStage.getAddSubTitle() != null) {
                    subtitle = aStage.getAddSubTitle();
                }
            } else if (operation == PlayerStage.Operation.REMOVE) {
                if (aStage.getRemoveTitle() != null) {
                    title = aStage.getRemoveTitle();
                }

                if (aStage.getRemoveSubTitle() != null) {
                    subtitle = aStage.getRemoveSubTitle();
                }
            }
        } else {
            if (AStagesCommon.ENABLE_TITLE_AFTER_STAGE_ADDING.get()) {
                fadeIn = 20;
                stay = 60;
                fadeOut = 20;

                if (operation == PlayerStage.Operation.ADD) {
                    title = Component.translatable("title.astages.add", stageToDescription(stage)).withStyle(AStagesCommon.TITLE_COLOR.get());
                }
            }
        }

        player.connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
        player.connection.send(new ClientboundSetTitleTextPacket(title));
        player.connection.send(new ClientboundSetSubtitleTextPacket(subtitle));
    }

    @Contract("_ -> !null")
    public static @NotNull String stageToDescription(@NotNull String input) {
        return capitalizeWords(input.replace('_', ' '));
    }

    @Contract("_ -> !null")
    public static @NotNull String structureToDescription(@NotNull ResourceLocation input) {
        return capitalizeWords(input.getPath().replace('_', ' '));
    }

    public static @NotNull String dimensionToDescription(@NotNull ResourceLocation input) {
        return capitalizeWords(input.getPath().replace('_', ' '));
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
        var playerStage = player.getData(AProvider.PLAYER_STAGE);
        return playerStage.getStages().contains(stage);
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

    public static boolean itemStacksMatchesIgnoringCount(ItemStack stack, ItemStack other) {
        if (stack == other) {
            return true;
        } else {
            return ItemStack.isSameItemSameComponents(stack, other);
        }
    }
}
