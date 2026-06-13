package com.alessandro.astages.infrastructure.advancement;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.advancement.StageMatcher;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.util.AStagesUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class StageEarnTrigger extends SimpleCriterionTrigger<StageEarnTrigger.TriggerInstance> {
    public static final ResourceLocation ID = AResourceLocation.fromNamespaceAndPath( "stage_earn");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext context) {
        StageMatcher stage = null;
        if (json.has("stage")) {
            stage = StageMatcher.CODEC.parse(JsonOps.INSTANCE, json.get("stage")).result().orElse(null);
        }

        List<StageMatcher> stages = null;
        if (json.has("stages")) {
            List<StageMatcher> matchers = new ArrayList<>();
            JsonArray array = GsonHelper.getAsJsonArray(json, "stages");
            for (JsonElement element : array) {
                StageMatcher.CODEC.parse(JsonOps.INSTANCE, element).result().ifPresent(matchers::add);
            }
            if (!matchers.isEmpty()) {
                stages = matchers;
            }
        }

        boolean checkServerStages = GsonHelper.getAsBoolean(json, "checkServerStages", false);

        return new TriggerInstance(player, stage, stages, checkServerStages);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> instance.matches(player));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        @Nullable private final StageMatcher stage;
        @Nullable private final List<StageMatcher> stages;
        private final boolean checkServerStages;

        public TriggerInstance(ContextAwarePredicate player, @Nullable StageMatcher stage, @Nullable List<StageMatcher> stages, boolean checkServerStages) {
            super(StageEarnTrigger.ID, player);
            this.stage = stage;
            this.stages = stages;
            this.checkServerStages = checkServerStages;
        }

        public boolean matches(ServerPlayer player) {
            var activeStages = checkServerStages ? AStagesUtils.getStages(AHolder.server()) : AStagesUtils.getStages(AHolder.player(player));

            if (stage != null && activeStages.stream().noneMatch(stage::match)) {
                return false;
            }

            if (stages != null) {
                for (StageMatcher matcher : stages) {
                    if (activeStages.stream().noneMatch(matcher::match)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}