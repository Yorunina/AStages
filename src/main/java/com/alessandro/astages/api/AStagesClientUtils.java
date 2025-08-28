package com.alessandro.astages.api;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.holder.AStageHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.capability.ClientServerStageData;
import com.alessandro.astages.core.AClientRestrictionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AStagesClientUtils {
    public static Set<String> getStages(AClientHolder holder) {
        return holder.getStages().getAllStages();
    }

    public static void setStages(AClientHolder holder, List<String> stages) {
        holder.perform(
            () -> ClientPlayerStage.setClientStages(stages),
            () -> ClientServerStageData.setServerStages(stages)
        );
    }

    public static boolean hasStage(AClientHolder holder, String stage) {
        return getStages(holder).contains(stage);
    }

    public static boolean hasStage(AClientHolder holder, AStageType type, String stage) {
        return holder.getStages().getForType(type).contains(stage);
    }

    public static boolean hasStage(AStageHolder holder, AStageType type, String stage) {
        return holder.getForType(type).contains(stage);
    }

    public static boolean hasAtLeastOneStage(AClientHolder holder, List<String> stages) {
        return getStages(holder).stream().anyMatch(stages::contains);
    }

    public static boolean hasAllStages(AClientHolder holder, List<String> stages) {
        return getStages(holder).containsAll(stages);
    }

    public static void addStage(AClientHolder holder, String stage) {
        holder.perform(
            () -> ClientPlayerStage.addClientStage(stage),
            () -> ClientServerStageData.addServerStage(stage)
        );
    }

    public static void addStages(AClientHolder holder, List<String> stages) {
        holder.perform(
            () -> ClientPlayerStage.addClientStages(stages),
            () -> ClientServerStageData.addServerStages(stages)
        );
    }

    public static void addAllStages(AClientHolder holder) {
        var stages = new ArrayList<>(AClientRestrictionManager.ALL_STAGES);

        holder.perform(
            () -> ClientPlayerStage.addClientStages(stages),
            () -> ClientServerStageData.addServerStages(stages)
        );
    }

    public static void removeStage(AClientHolder holder, String stage) {
        holder.perform(
            () -> ClientPlayerStage.removeClientStage(stage),
            () -> ClientServerStageData.removeServerStage(stage)
        );
    }

    public static void removeStages(AClientHolder holder, List<String> stages) {
        holder.perform(
            () -> ClientPlayerStage.removeClientStages(stages),
            () -> ClientServerStageData.removeServerStages(stages)
        );
    }

    public static void removeAllStages(AClientHolder holder) {
        holder.perform(
            () -> {
                var stages = ClientPlayerStage.getClientStages();
                ClientPlayerStage.removeClientStages(stages);
            },
            () -> {
                var stages = ClientServerStageData.getServerStages();
                ClientServerStageData.removeServerStages(stages);
            }
        );
    }
}
