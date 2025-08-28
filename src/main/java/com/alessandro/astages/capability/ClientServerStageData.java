package com.alessandro.astages.capability;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientServerStageData {
    private static List<String> serverStages = new ArrayList<>();

    public static List<String> getServerStages() {
        return serverStages;
    }

    public static void setServerStages(List<String> stages) {
        ClientServerStageData.serverStages = stages;
    }

    public static void addServerStage(String stage) {
        serverStages.add(stage);
    }

    public static void addServerStages(List<String> stages) {
        serverStages.addAll(stages);
    }

    public static void removeServerStage(String stage) {
        serverStages.remove(stage);
    }

    public static void removeServerStages(List<String> stages) {
        serverStages.removeAll(stages);
    }
}
