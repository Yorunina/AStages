package com.alessandro.astages.api;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.holder.AStageHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.capability.OfflinePlayerStage;
import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.stage.AStageManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public class AStagesUtils {
    public static Set<String> getStages(AHolder holder) {
        return holder.getStages().getAllStages();
    }

    public static boolean hasStage(AHolder holder, String stage) {
        return getStages(holder).contains(stage);
    }

    public static boolean hasStage(AHolder holder, AStageType type, String stage) {
        return holder.getStages().getForType(type).contains(stage);
    }

    public static boolean hasStage(AStageHolder holder, AStageType type, String stage) {
        return holder.getForType(type).contains(stage);
    }

    public static boolean hasAtLeastOneStage(AHolder holder, List<String> stages) {
        return getStages(holder).stream().anyMatch(stages::contains);
    }

    public static boolean hasAllStages(AHolder holder, List<String> stages) {
        return getStages(holder).containsAll(stages);
    }

    public static void addStage(AHolder holder, String stage) {
        holder.perform(
            player -> {
                OfflinePlayerStage.addPlayerStage(player, stage);
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD, stage, false);
            }, server -> {
                var data = ServerStageData.getData(server);
                data.addServerStage(stage);
                data.synchronizeWithClient(null, AOperation.ADD, stage);
            }
        );
    }

    public static void addStages(AHolder holder, List<String> stages) {
        holder.perform(
            player -> {
                OfflinePlayerStage.addPlayerStages(player, stages);
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD_ALL, stages, false);
            }, server -> {
                var data = ServerStageData.getData(server);
                data.addServerStages(stages);
                data.synchronizeWithClient(null, AOperation.ADD_ALL, stages);
            }
        );
    }

    public static void addAllStages(AHolder holder) {
        var stages = new ArrayList<>(ARestrictionManager.ALL_STAGES);

        holder.perform(
            player -> {
                OfflinePlayerStage.addPlayerStages(player, stages);
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD_ALL, stages, false);
            }, server -> {
                var data = ServerStageData.getData(server);
                data.addServerStages(stages);
                data.synchronizeWithClient(null, AOperation.ADD_ALL, stages);
            }
        );
    }

    public static void removeStage(AHolder holder, String stage) {
        holder.perform(
            player -> {
                OfflinePlayerStage.removePlayerStage(player, stage);
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE, stage, false);
            }, server -> {
                var data = ServerStageData.getData(server);
                data.removeServerStage(stage);
                data.synchronizeWithClient(null, AOperation.REMOVE, stage);
            }
        );
    }

    public static void removeStages(AHolder holder, List<String> stages) {
        holder.perform(
            player -> {
                OfflinePlayerStage.removePlayerStages(player, stages);
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE_ALL, stages, false);
            }, server -> {
                var data = ServerStageData.getData(server);
                data.removeServerStages(stages);
                data.synchronizeWithClient(null, AOperation.REMOVE_ALL, stages);
            }
        );
    }

    public static void removeAllStages(AHolder holder) {
        holder.perform(
            player -> {
                var stages = OfflinePlayerStage.getPlayerStagesFromFile(player);
                OfflinePlayerStage.removePlayerStages(player, stages);
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE_ALL, stages, false);
            }, server -> {
                var data = ServerStageData.getData(server);
                var stages = data.getServerStages();
                data.removeServerStages(stages);
                data.synchronizeWithClient(null, AOperation.REMOVE_ALL, stages);
            }
        );
    }

    public static void checkPlayerStages(@Nullable Player player, AOperation operation, List<String> stages) {
        checkStages(player, stage -> {
            if (AStageManager.isServerOnly(stage)) {
                throw new IllegalArgumentException("Trying to add stage " + stage + " that is marked as available in server only!");
            }
        }, operation, stages);
    }

    public static void checkServerStages(AOperation operation, List<String> stages) {
        checkStages(ServerLifecycleHooks.getCurrentServer(), stage -> {
            if (AStageManager.isPlayerOnly(stage)) {
                throw new IllegalArgumentException("Trying to add stage " + stage + " that is marked as available in player only!");
            }
        }, operation, stages);
    }

    public static void checkStages(@Nullable CommandSource chatSource, Consumer<String> checker, AOperation operation, List<String> stages) {
        if (operation.supportOnlyOneStage() && stages.size() != 1) {
            throw new IllegalArgumentException("Trying to perform an action that supports single stage, using multiple ones!");
        }

        if (operation.needToBeChecked()) {
            for (var stage : stages) {
                checker.accept(stage);
            }
        }

        if (operation == AOperation.LOGIN) { return; }
        if (chatSource == null) { return; }

        for (var stage : stages) {
            if (!ARestrictionManager.ALL_STAGES.contains(stage)) {
                chatSource.sendSystemMessage(Component.literal("⚠️ Warning: stage " + stage + " not recognized!").withStyle(ChatFormatting.GOLD));
            }
        }
    }
}
