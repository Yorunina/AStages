package com.alessandro.astages.core.stage;

import com.alessandro.astages.core.ARestrictionManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StageArrayList<E extends AStage> extends ArrayList<E> {
    @Override
    public boolean add(@NotNull E e) {
        if (!ARestrictionManager.ALL_STAGES.add(e.stage)) {
//            AStages.LOGGER.warn("Registered stage without attached restriction: {}!", e.stage);
        }

        return super.add(e);
    }
}
