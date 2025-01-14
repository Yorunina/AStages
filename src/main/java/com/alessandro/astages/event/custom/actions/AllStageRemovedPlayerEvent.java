package com.alessandro.astages.event.custom.actions;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;

public class AllStageRemovedPlayerEvent extends PlayerEvent {
    public final List<String> stages;

    public AllStageRemovedPlayerEvent(Player player, List<String> stages) {
        super(player);
        this.stages = stages;
    }
}
