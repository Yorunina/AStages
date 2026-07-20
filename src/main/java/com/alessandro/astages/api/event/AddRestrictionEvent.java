package com.alessandro.astages.api.event;

import com.alessandro.astages.api.constant.AEventPhase;
import net.minecraftforge.eventbus.api.Event;

public class AddRestrictionEvent extends Event {
    private final AEventPhase phase;

    public AddRestrictionEvent(AEventPhase phase) {
        this.phase = phase;
    }

    public AEventPhase getEventPhase() {
        return phase;
    }
}
