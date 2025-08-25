package com.alessandro.astages.api.holder;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import org.jetbrains.annotations.Contract;

import java.util.*;

@NotNullMethodsReturn
public class AStageHolder {
    private final Map<AStageType, HashSet<String>> stages = new HashMap<>();

    @Contract(" -> new")
    public static AStageHolder init() {
        return new AStageHolder();
    }

    public static AStageHolder initAndHold(AStageType type, Collection<String> stages) {
        return AStageHolder.init().hold(type, stages);
    }

    public AStageHolder hold(AStageType type, Collection<String> stages) {
        this.stages.put(type, new HashSet<>(stages));
        return this;
    }

    public HashSet<String> getForType(AStageType type) {
        return stages.getOrDefault(type, new HashSet<>());
    }
}
