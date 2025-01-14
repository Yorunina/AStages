package com.alessandro.astages.event.custom.actions;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class StageRemovedPlayerEvent extends PlayerEvent {
    public final String stage;

    public StageRemovedPlayerEvent(Player player, String stage) {
        super(player);
        this.stage = stage;
    }
}
