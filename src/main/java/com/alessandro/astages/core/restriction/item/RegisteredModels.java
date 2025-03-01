package com.alessandro.astages.core.restriction.item;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class RegisteredModels {
    private final HashMap<ResourceLocation, AModel<?>> MODELS = new HashMap<>();

    public <T> AModel<T> registerModel(ResourceLocation id, AModel<T> model) {
        // if (MODELS.containsKey(id)) { AStages.LOGGER.error("Model with ID {} already found in registered models!", id); }
        if (MODELS.containsKey(id)) { return null; }
        MODELS.put(id, model);
        return model;
    }

    @SuppressWarnings("unchecked")
    public <T> AModel<T> getModel(ResourceLocation id, Class<T> clazz) {
        return (AModel<T>) MODELS.get(id);
    }

    public AModel<?> getModel(ResourceLocation id) {
        return MODELS.get(id);
    }
}
