package com.alessandro.astages.command;

import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.command.argument.AStagesServerRemoveArgument;
import com.alessandro.astages.api.annotation.nullability.NotNullParams;
import com.alessandro.astages.api.annotation.nullability.Nullable;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

@NotNullParams
public class AStagesServerCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("astages").requires(c -> c.hasPermission(2)).then(Commands.literal("server")
            .then(Commands.literal("add").then(Commands.argument("stage", StringArgumentType.string())
                .executes(context -> addServerStageCommand(context.getSource().getServer(), StringArgumentType.getString(context, "stage")))
            ))
            .then(Commands.literal("remove").then(Commands.argument("stage", AStagesServerRemoveArgument.stages())
                .executes(context -> removeServerStageCommand(context.getSource().getServer(), AStagesServerRemoveArgument.getStage(context, "stage")))
            ))
            .then(Commands.literal("remove_all")
                .executes(context -> removeAllServerStageCommand(context.getSource().getServer()))
            )
            .then(Commands.literal("info")
                .executes(context -> infoCommand(context.getSource().getServer(), context.getSource().getPlayer()))
            )
        ));
    }

    private static int addServerStageCommand(MinecraftServer server, String stageToAdd) {
        ServerStageData.getData(server).add(stageToAdd);
        return 1;
    }

    private static int removeServerStageCommand(MinecraftServer server, String stageToRemove) {
        ServerStageData.getData(server).remove(stageToRemove);
        return 1;
    }

    private static int removeAllServerStageCommand(MinecraftServer server) {
        ServerStageData.getData(server).removeAll();
        return 1;
    }

    private static int infoCommand(MinecraftServer server, @Nullable ServerPlayer executor) {
        var serverStage = ServerStageData.getData(server).get();

        if (executor != null) {
            if (serverStage.isEmpty()) {
                executor.sendSystemMessage(Component.translatable("chat.astages.info.server.no_stages").withStyle(ChatFormatting.RED));
            } else {
                executor.sendSystemMessage(Component.translatable("chat.astages.info.server.has_stages").withStyle(ChatFormatting.GREEN));
                for (var stage : serverStage) {
                    executor.sendSystemMessage(Component.translatable("chat.astages.info.server.list_item", stage));
                }
            }
        }

        return 1;
    }
}
