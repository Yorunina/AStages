package com.alessandro.astages.plugin.container;

import com.alessandro.astages.store.server.AMinimalManager;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;

import java.util.HashMap;
import java.util.Map;

@NotNullMethodsReturn
public class ManagerContainer {
    private final Map<Object, AMinimalManager<?>> MANAGERS = new HashMap<>();

    public static ManagerContainer initialize() {
        return new ManagerContainer();
    }

    public void register(Object type, AMinimalManager<?> manager) {
        MANAGERS.put(type, manager);
    }

    public Map<Object, AMinimalManager<?>> get() {
        return MANAGERS;
    }
}
