package com.alessandro.astages.core.stage;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.BaseStage;
import com.alessandro.astages.api.stage.Stage;
import com.alessandro.astages.api.stage.TemporaryStage;
import com.alessandro.astages.api.stage.implementation.AExpirable;
import com.alessandro.astages.api.stage.implementation.AGrantable;
import com.alessandro.astages.core.ARestrictionManager;

import java.util.*;

@Deprecated(forRemoval = true)
@NotNullParamsAndMethodsReturn
public class AStageManager {
    private static final Map<String, BaseStage<?>> STAGES = new HashMap<>();
    private static final Map<String, TemporaryStage> TEMPORARY_STAGES = new HashMap<>();

    public static void reloadBeforeScripts() {
//        STAGES.clear();
//        TEMPORARY_STAGES.clear();
    }

    public static void reloadAfterScripts() {
        STAGES.values().forEach(stage -> {
            if (!stage.isServerOnly()) {
                ARestrictionManager.ALL_STAGES.add(stage.getStage());
            }
        });
    }

    public static boolean isServerOnly(String stageKey) {
        var stage = getStage(stageKey);
        if (stage != null) { return stage.isServerOnly(); }

        return false;
    }

    public static boolean isPlayerOnly(String stageKey) {
        var stage = getStage(stageKey);
        if (stage != null) { return stage.isPlayerOnly(); }

        return false;
    }

    public static void addStage(Stage stage) {
        if (checkForDuplicates(stage)) {
            STAGES.put(stage.getStage(), stage);
        }
    }

    public static void addTemporaryStage(TemporaryStage stage) {
        if (checkForDuplicates(stage)) {
            STAGES.put(stage.getStage(), stage);
            TEMPORARY_STAGES.put(stage.getStage(), stage);
        }
    }

    public static boolean checkForDuplicates(BaseStage<?> stage) {
        if (STAGES.containsKey(stage.getStage())) {
            AStages.LOGGER.warn("Trying to modify stage {} twice! Operation not allowed!", stage.getStage());
            return false;
        }

        return true;
    }

    public static @Nullable BaseStage<?> getStage(String stageKey) {
        return STAGES.getOrDefault(stageKey, null);
    }

    public static boolean isTemporary(String stage) {
        return TEMPORARY_STAGES.containsKey(stage);
    }

    public static TemporaryStage getTemporaryStage(String stage) {
        return TEMPORARY_STAGES.get(stage);
    }

    public static Set<AGrantable> getStagesWithCustomGrantedEvent(List<String> stageKeys) {
        var toReturn = new HashSet<AGrantable>();

        for (var stageKey : stageKeys) {
            var stage = getStage(stageKey);

            if (stage != null && stage.hasCustomGrantedEvent()) {
                toReturn.add(stage);
            }
        }

        return toReturn;
    }

    public static Set<AExpirable> getStagesWithCustomExpiredEvent(List<String> stageKeys) {
        var toReturn = new HashSet<AExpirable>();

        for (var stageKey : stageKeys) {
            var stage = getStage(stageKey);

            if (stage instanceof AExpirable expirable && expirable.hasCustomExpiredEvent()) {
                toReturn.add(expirable);
            }
        }

        return toReturn;
    }

//    static {
//        var buff = new TemporaryStage("stage", Duration.ofSeconds(10))
//            .whenGranted(event -> {
//                event.getPlayer().sendSystemMessage(Component.literal("New Buff Unlocked!"));
//                event.getPlayer().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10000, 255, false, false, false));
//            })
//            .whenExpired(event -> {
//                event.getPlayer().sendSystemMessage(Component.literal("Buff Expired!"));
//                event.getPlayer().removeEffect(MobEffects.REGENERATION);
//            });
//
//        AStageManager.addTemporaryStage(buff);
//    }
}
