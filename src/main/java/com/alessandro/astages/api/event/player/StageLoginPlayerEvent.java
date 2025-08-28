package com.alessandro.astages.api.event.player;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.List;

public class StageLoginPlayerEvent extends PlayerEvent {
    public final List<String> stages;

    public StageLoginPlayerEvent(Player player, List<String> stages) {
        super(player);
        this.stages = stages;
    }
}
