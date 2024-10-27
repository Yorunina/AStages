package com.alessandro.astages.integration.kubejs.event;

import com.alessandro.astages.event.custom.actions.StageRemovedPlayerEvent;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.player.Player;

public class StageRemovedEventJS extends PlayerEventJS {
    StageRemovedPlayerEvent event;

    public StageRemovedEventJS(StageRemovedPlayerEvent event) {
        this.event = event;
    }

    @Override
    public Player getEntity() {
        return event.getEntity();
    }

    public String getStage() {
        return event.stage;
    }
}
