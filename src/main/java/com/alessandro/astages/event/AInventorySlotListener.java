package com.alessandro.astages.event;

import com.alessandro.astages.event.custom.PlayerInventoryChangedEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

public record AInventorySlotListener(Player player) implements ContainerListener {
    @Override
    public void slotChanged(@NotNull AbstractContainerMenu container, int index, @NotNull ItemStack stack) {
        if (!stack.isEmpty() && container.getSlot(index).container == player.getInventory()) {
            MinecraftForge.EVENT_BUS.post(new PlayerInventoryChangedEvent(player, stack, index));
        }
    }

    @Override
    public void dataChanged(@NotNull AbstractContainerMenu container, int index, int item) { }
}
