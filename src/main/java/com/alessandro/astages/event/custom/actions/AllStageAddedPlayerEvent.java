package com.alessandro.astages.event.custom.actions;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.List;

public class AllStageAddedPlayerEvent extends PlayerEvent {
    public final List<String> stages;

    public AllStageAddedPlayerEvent(Player player, List<String> stages) {
        super(player);
        this.stages = stages;
    }
}
