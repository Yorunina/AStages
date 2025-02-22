package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.capability.ClientPlayerStage;

import java.util.List;

public class AStagesClientJSUtil {
    public static List<String> getClientStages() {
        return ClientPlayerStage.getPlayerStages();
    }
}
