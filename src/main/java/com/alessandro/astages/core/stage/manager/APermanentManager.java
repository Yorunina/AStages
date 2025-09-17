package com.alessandro.astages.core.stage.manager;

import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.Stage;
import com.alessandro.astages.core.AStageManager;

import java.util.HashMap;
import java.util.Map;

public class APermanentManager {
    private final Map<String, Stage> STAGES = new HashMap<>();

    public Map<String, Stage> getStages() {
        return STAGES;
    }

    public void reloadBeforeScripts() {
        STAGES.clear();
    }

    public void addStage(Stage stage) {
        if (AStageManager.GENERIC_INSTANCE.checkForDuplicates(stage)) {
            AStageManager.GENERIC_INSTANCE.addStageNoCheck(stage);
            STAGES.put(stage.getStage(), stage);
        }
    }

    public @Nullable Stage getStage(String stageKey) {
        return STAGES.getOrDefault(stageKey, null);
    }
}
