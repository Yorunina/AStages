package com.alessandro.astages.capability;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientPlayerStage {
    private static List<String> playerStages = new ArrayList<>();

    public static List<String> getClientStages() {
        return playerStages;
    }

    public static void setClientStages(List<String> stages) {
        ClientPlayerStage.playerStages = stages;
    }

    public static void addClientStage(String stage) {
        playerStages.add(stage);
    }

    public static void addClientStages(List<String> stages) {
        playerStages.addAll(stages);
    }

    public static void removeClientStage(String stage) {
        playerStages.remove(stage);
    }

    public static void removeClientStages(List<String> stages) {
        playerStages.removeAll(stages);
    }

    @Deprecated(forRemoval = true)
    public static List<String> getPlayerStages() {
        return playerStages;
    }

    @Deprecated(forRemoval = true)
    public static boolean hasStage(String stage) {
        return playerStages.contains(stage);
    }

    @Deprecated(forRemoval = true)
    public static void addAllStages(List<String> stages) {
        ClientPlayerStage.playerStages.addAll(stages);
    }

    @Deprecated(forRemoval = true)
    public static void removeAllStages(List<String> stages) {
        ClientPlayerStage.playerStages.removeAll(stages);
    }
}
