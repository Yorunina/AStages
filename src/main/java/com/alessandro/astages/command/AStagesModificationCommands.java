package com.alessandro.astages.command;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.command.argument.AStagesAddArgument;
import com.alessandro.astages.command.argument.AStagesRemoveArgument;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class AStagesModificationCommands {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("astages").requires(c -> c.hasPermission(2))
            .then(Commands.literal("add").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("stage", AStagesAddArgument.stages())
                .executes(context -> addStageCommand(EntityArgument.getPlayer(context, "player"), AStagesAddArgument.getStage(context, "stage"), true, false))
            )))
            .then(Commands.literal("add").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("stage", AStagesAddArgument.stages()).then(Commands.argument("silentChat", BoolArgumentType.bool()).then(Commands.argument("silentTitle", BoolArgumentType.bool())
                .executes(context -> addStageCommand(EntityArgument.getPlayer(context, "player"), AStagesAddArgument.getStage(context, "stage"), BoolArgumentType.getBool(context, "silentChat"), BoolArgumentType.getBool(context, "silentTitle")))
            )))))
            .then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("stage", AStagesRemoveArgument.stages())
                .executes(context -> removeStageCommand(EntityArgument.getPlayer(context, "player"), AStagesRemoveArgument.getStage(context, "stage"), false, true))
            )))
            .then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("stage", AStagesRemoveArgument.stages()).then(Commands.argument("silentChat", BoolArgumentType.bool()).then(Commands.argument("silentTitle", BoolArgumentType.bool())
                .executes(context -> removeStageCommand(EntityArgument.getPlayer(context, "player"), AStagesRemoveArgument.getStage(context, "stage"), BoolArgumentType.getBool(context, "silentChat"), BoolArgumentType.getBool(context, "silentTitle")))
            )))))
            .then(Commands.literal("remove_all").then(Commands.argument("player", EntityArgument.player())
                .executes(context -> removeAllStagesCommand(EntityArgument.getPlayer(context, "player"), false, true))
            ))
            .then(Commands.literal("remove_all").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("silentChat", BoolArgumentType.bool()).then(Commands.argument("silentTitle", BoolArgumentType.bool())
                .executes(context -> removeAllStagesCommand(EntityArgument.getPlayer(context, "player"), BoolArgumentType.getBool(context, "silentChat"), BoolArgumentType.getBool(context, "silentTitle")))
            ))))
            .then(Commands.literal("info")
                .executes(context -> infoCommand(Objects.requireNonNull(context.getSource().getPlayer())))
            )
            .then(Commands.literal("info").then(Commands.argument("player", EntityArgument.player())
                .executes(context -> infoCommand(EntityArgument.getPlayer(context, "player")))
            ))
        );
    }

    private static int addStageCommand(Player player, String stageToAdd, boolean silentChat, boolean silentTitle) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.addStage(stageToAdd);

            if (!silentChat) {
                player.sendSystemMessage(Component.literal("Stage \"" + stageToAdd + "\" added successfully!").withStyle(ChatFormatting.GREEN));
            }

            playerStage.setChangedFor(player, PlayerStage.Operation.ADD, stageToAdd, silentTitle);
        });

        return 1;
    }

    private static int removeStageCommand(Player player, String stageToRemove, boolean silentChat, boolean silentTitle) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            if (playerStage.removeStage(stageToRemove) == PlayerStage.Status.SUCCESS) {
                if (!silentChat) {
                    player.sendSystemMessage(Component.literal("Stage \"" + stageToRemove + "\" removed successfully!").withStyle(ChatFormatting.GREEN));
                }

                playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE, stageToRemove, silentTitle);
            } else {
                if (!silentChat) {
                    player.sendSystemMessage(Component.literal("Stage \"" + stageToRemove + "\" is not present in your stages!").withStyle(ChatFormatting.RED));
                }
            }
        });

        return 1;
    }

    private static int removeAllStagesCommand(Player player, boolean silentChat, boolean silentTitle) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.removeAllStages();

            if (!silentChat) {
                player.sendSystemMessage(Component.literal("All stages removed successfully!").withStyle(ChatFormatting.GREEN));
            }

            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE_ALL, null, silentTitle);
        });

        return 1;
    }

    private static int infoCommand(Player player) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            if (playerStage.getStages().isEmpty()) {
                player.sendSystemMessage(Component.literal("No stages unlocked for player ").append(player.getName()).append("!").withStyle(ChatFormatting.RED));
            } else {
                player.sendSystemMessage(Component.literal("Stages unlocked by ").append(player.getName()).append(":").withStyle(ChatFormatting.GREEN));
                for (var stage : playerStage.getStages()) {
                    player.sendSystemMessage(Component.literal(" - ").append(stage));
                }
            }
        });

        return 1;
    }
}
