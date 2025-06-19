package com.alessandro.astages.event.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Fired just before {@link Mob#mobInteract(Player, InteractionHand)} happens.
 */
@Cancelable
public class MobInteractEvent extends PlayerEvent {
    private final InteractionHand hand;
    private final EntityType<?> mobType;

    public MobInteractEvent(Player player, InteractionHand hand, EntityType<?> mobType) {
        super(player);
        this.hand = hand;
        this.mobType = mobType;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public EntityType<?> getMobType() {
        return mobType;
    }
}
