package com.alessandro.astages.api;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.holder.AStageHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.capability.ClientPlayerStage;

import java.util.List;

@NotNullParamsAndMethodsReturn
public class AStagesClientUtils {
    public static List<String> getStages() {
        return ClientPlayerStage.getPlayerStages();
    }

    public static boolean hasStage(AClientHolder holder, AStageType type, String stage) {
        return holder.stages().getForType(type).contains(stage);
    }

    public static boolean hasStage(AStageHolder holder, AStageType type, String stage) {
        return holder.getForType(type).contains(stage);
    }
}
