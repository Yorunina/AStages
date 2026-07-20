package com.alessandro.astages.engine.client.stage;

import com.alessandro.astages.api.stage.ClientTemporaryStage;
import com.alessandro.astages.engine.AClientStageManager;
import com.alessandro.astages.api.stage.AStageClientBaseManager;

public class AClientTemporaryManager extends AStageClientBaseManager<ClientTemporaryStage> {
    public void addStage(ClientTemporaryStage stage) {
        AClientStageManager.GENERIC_INSTANCE.addStageNoCheck(stage);
        addStageInternal(stage.getStage(), stage);
    }
}
