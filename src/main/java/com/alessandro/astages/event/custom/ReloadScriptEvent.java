package com.alessandro.astages.event.custom;

import net.minecraftforge.eventbus.api.Event;

public class ReloadScriptEvent extends Event {
    private final EventScriptType scriptType;

    public ReloadScriptEvent(EventScriptType scriptType) {
        this.scriptType = scriptType;
    }

    public static class BeforeScriptsLoaded extends ReloadScriptEvent {
        public BeforeScriptsLoaded(EventScriptType scriptType) {
            super(scriptType);
        }
    }

    public static class AfterScriptsLoaded extends ReloadScriptEvent {
        public AfterScriptsLoaded(EventScriptType scriptType) {
            super(scriptType);
        }
    }

    public EventScriptType getScriptType() {
        return scriptType;
    }

    public enum EventScriptType {
        CLIENT,
        SERVER,
        STARTUP
    }
}
