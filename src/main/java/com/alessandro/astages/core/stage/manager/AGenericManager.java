package com.alessandro.astages.core.stage.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.BaseStage;
import com.alessandro.astages.api.stage.implementation.AGrantable;
import com.alessandro.astages.core.ARestrictionManager;

import java.util.*;

@NotNullParams
public class AGenericManager {
    private final Map<String, BaseStage<?>> STAGES = new HashMap<>();

    public Map<String, BaseStage<?>> getStages() {
        return STAGES;
    }

    public void reloadBeforeScripts() {
        STAGES.clear();
    }

    public void reloadAfterScripts() {
        STAGES.values().forEach(stage -> {
            if (!stage.isServerOnly()) {
                ARestrictionManager.ALL_STAGES.add(stage.getStage());
            }
        });
    }

    public void addStage(BaseStage<?> stage) {
        if (checkForDuplicates(stage)) {
            STAGES.put(stage.getStage(), stage);
        }
    }

    public void addStageNoCheck(BaseStage<?> stage) {
        STAGES.put(stage.getStage(), stage);
    }

    public boolean checkForDuplicates(BaseStage<?> stage) {
        if (STAGES.containsKey(stage.getStage())) {
            AStages.LOGGER.warn("Trying to modify stage {} twice! Operation not allowed!", stage.getStage());
            return false;
        }

        return true;
    }

    public @Nullable BaseStage<?> getStage(String stageKey) {
        return STAGES.getOrDefault(stageKey, null);
    }

    public Set<AGrantable> getStagesWithCustomGrantedEvent(List<String> stageKeys) {
        var toReturn = new HashSet<AGrantable>();

        for (var stageKey : stageKeys) {
            var stage = getStage(stageKey);

            if (stage != null && stage.hasCustomGrantedEvent()) {
                toReturn.add(stage);
            }
        }

        return toReturn;
    }

    public boolean isServerOnly(String stageKey) {
        var stage = getStage(stageKey);
        if (stage != null) { return stage.isServerOnly(); }

        return false;
    }

    public boolean isPlayerOnly(String stageKey) {
        var stage = getStage(stageKey);
        if (stage != null) { return stage.isPlayerOnly(); }

        return false;
    }
}
