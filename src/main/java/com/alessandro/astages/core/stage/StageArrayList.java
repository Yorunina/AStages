package com.alessandro.astages.core.stage;

import com.alessandro.astages.AStages;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StageArrayList<E extends AStage> extends ArrayList<E> {
    @Override
    public boolean add(@NotNull E e) {
//        if (!ARestrictionManager.ALL_STAGES.add(e.getStage())) {
//            AStages.LOGGER.warn("Registered stage without attached restriction: {}!", e.stage);
//        }

        if (AStageManager.getStages().contains(e)) {
            AStages.LOGGER.warn("Trying to modify stage {} twice! Operation not allowed!", e.getStage());
            return false;
        }

        return super.add(e);
    }

//    @Override
//    public boolean contains(Object o) {
//        if (o instanceof String stage) {
//            for (E e : this) {
//                if (e.getStage().equals(stage)) {
//                    return true;
//                }
//            }
//        }
//
//        return super.contains(o);
//    }
}
