package com.alessandro.astages.event.custom;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerInventoryChangedEvent extends PlayerEvent {
    private final ItemStack item;
    private final int slot;

    public PlayerInventoryChangedEvent(Player player, ItemStack item, int slot) {
        super(player);
        this.item = item;
        this.slot = slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }
}
