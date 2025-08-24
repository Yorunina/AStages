package com.alessandro.astages.api;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.capability.OfflinePlayerStage;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.stage.AStageManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class AStagesUtils {
    public static List<String> getStages(Player player) {
        return OfflinePlayerStage.getPlayerStagesFromFile(player);
    }

    public static boolean hasStage(Player player, String stage) {
        return OfflinePlayerStage.getPlayerStagesFromFile(player).contains(stage);
    }

    public static boolean hasAtLeastOneStage(Player player, List<String> stages) {
        return OfflinePlayerStage.getPlayerStagesFromFile(player).stream().anyMatch(stages::contains);
    }

    public static boolean hasAllStages(Player player, List<String> stages) {
        return new HashSet<>(OfflinePlayerStage.getPlayerStagesFromFile(player)).containsAll(stages);
    }

    public static void addStage(Player player, String stage) {
        OfflinePlayerStage.addPlayerStage(player, stage);
        OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD, stage, false);
    }

    public static void addStages(Player player, List<String> stages) {
        OfflinePlayerStage.addPlayerStages(player, stages);
        OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD_ALL, stages, false);
    }

    public static void addAllStages(Player player) {
        var stages = new ArrayList<>(ARestrictionManager.ALL_STAGES);
        OfflinePlayerStage.addPlayerStages(player, stages);
        OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD_ALL, stages, false);
    }

    public static void removeStage(Player player, String stage) {
        OfflinePlayerStage.removePlayerStage(player, stage);
        OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE, stage, false);
    }

    public static void removeStages(Player player, List<String> stages) {
        OfflinePlayerStage.removePlayerStages(player, stages);
        OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE_ALL, stages, false);
    }

    public static void removeAllStages(Player player) {
        var stages = OfflinePlayerStage.getPlayerStagesFromFile(player);
        OfflinePlayerStage.removePlayerStages(player, stages);
        OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE_ALL, stages, false);
    }

    public static void checkStages(@Nullable Player player, AOperation operation, List<String> stages) {
        if (operation.supportOnlyOneStage() && stages.size() != 1) {
            throw new IllegalArgumentException("Trying to perform an action that supports single stage, using multiple ones!");
        }

        if (operation.needToBeChecked()) {
            for (var stage : stages) {
                if (AStageManager.isServerOnly(stage)) {
                    throw new IllegalArgumentException("Trying to add stage " + stage + " that is marked as available in server only!");
                }
            }
        }

        if (operation == AOperation.LOGIN) { return; }
        if (player == null) { return; }

        for (var stage : stages) {
            if (!ARestrictionManager.ALL_STAGES.contains(stage)) {
                player.sendSystemMessage(Component.literal("⚠️ Warning: stage " + stage + " not recognized!").withStyle(ChatFormatting.GOLD));
            }
        }
    }
}
