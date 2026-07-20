package com.alessandro.astages.api;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.function.Consumer;

public class ALoader {
    public static final EventBus EVENT_BUS = new EventBus();

    public static class EventBus {
        public void post(Event event) {
            MinecraftForge.EVENT_BUS.post(event);
        }

        public <T extends Event> void addListener(EventPriority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer) {
            MinecraftForge.EVENT_BUS.addListener(priority, receiveCancelled, eventType, consumer);
        }

        public <T extends Event> void addListener(Consumer<T> consumer) {
            MinecraftForge.EVENT_BUS.addListener(consumer);
        }
    }
}
