package com.alessandro.astages.util;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.stage.AStageManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
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
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AStagesUtil {
    @SuppressWarnings("removal")
    public static ResourceLocation fromNamespaceAndPath(String path) {
        return new ResourceLocation(AStages.MODID, path);
    }

    @SuppressWarnings("removal")
    public static ResourceLocation parse(String location) {
        return new ResourceLocation(location);
    }

    public static void updateSelectedSlot(Player player) {
        updateSelectedSlot((ServerPlayer) player);
    }

    public static void updateSelectedSlot(ServerPlayer player) {
        // Synchronize changes with client!
        var slot = player.getInventory().selected;
        player.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, slot, player.getInventory().getItem(slot)));
    }

    public static @Nullable Player getNearestPlayer(Level level, BlockPos pos) {
        return getNearestPlayer(level, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static @Nullable Player getNearestPlayer(Level level, Vec3 pos) {
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
        Component chatMessage = null;
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

                if (aStage.getAddChatMessage() != null) {
                    chatMessage = aStage.getAddChatMessage();
                }
            } else if (operation == PlayerStage.Operation.REMOVE) {
                if (aStage.getRemoveTitle() != null) {
                    title = aStage.getRemoveTitle();
                }

                if (aStage.getRemoveSubTitle() != null) {
                    subtitle = aStage.getRemoveSubTitle();
                }

                if (aStage.getRemoveChatMessage() != null) {
                    chatMessage = aStage.getRemoveChatMessage();
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
        if (chatMessage != null) { player.sendSystemMessage(chatMessage); }
    }

    @Contract("_ -> !null")
    public static String stageToDescription(String input) {
        return capitalizeWords(input.replace('_', ' '));
    }

    @Contract("_ -> !null")
    public static String structureToDescription(ResourceLocation input) {
        return capitalizeWords(input.getPath().replace('_', ' '));
    }

    public static String dimensionToDescription(ResourceLocation input) {
        return capitalizeWords(input.getPath().replace('_', ' '));
    }

    public static String capitalizeWords(String input) {
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
    public static boolean hasStage(Player player, String stage) {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> toReturn.set(playerStage.getStages().contains(stage)));
        return toReturn.get();
    }

    public static @Nullable Player getPlayerFromUUID(MinecraftServer server, UUID uuid) {
        return server.getPlayerList().getPlayer(uuid);
    }

    public static BakedModel getBakedModelFromState(BlockState state) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
    }

    public static void setBakedModelForState(BlockState state, BakedModel bakedModel) {
        Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().modelByStateCache.put(state, bakedModel);
    }

    @Contract("_ -> new")
    public static ItemStack stateToStack(BlockState state) {
        return new ItemStack(state.getBlock());
    }

    @Contract("_ -> new")
    public static ItemStack blockToStack(Block block) {
        return new ItemStack(block);
    }

    public static boolean itemStacksMatchesIgnoringCount(ItemStack stack, ItemStack other) {
        if (stack == other) {
            return true;
        } else {
            return ItemStack.isSameItemSameTags(stack, other);
        }
    }
}
