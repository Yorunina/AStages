package com.alessandro.astages.core.stage.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.BaseStage;
import com.alessandro.astages.api.stage.implementation.AGrantable;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.stage.AStageBaseManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NotNullParams
public class AGenericManager extends AStageBaseManager<BaseStage<?>> {
    public void reloadAfterScripts() {
        getStages().values().forEach(stage -> {
            if (!stage.isServerOnly()) {
                ARestrictionManager.ALL_STAGES.add(stage.getStage());
            }
        });
    }

    public void addStage(BaseStage<?> stage) {
        if (checkForDuplicates(stage)) {
            addStageInternal(stage.getStage(), stage);
        }
    }

    public void addStageNoCheck(BaseStage<?> stage) {
        addStageInternal(stage.getStage(), stage);
    }

    public boolean checkForDuplicates(BaseStage<?> stage) {
        if (getStages().containsKey(stage.getStage())) {
            AStages.LOGGER.warn("Trying to modify stage {} twice! Operation not allowed!", stage.getStage());
            return false;
        }

        return true;
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
