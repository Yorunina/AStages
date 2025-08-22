package com.alessandro.astages.event;

import com.alessandro.astages.event.custom.ContainerChangedEvent;
import com.alessandro.astages.api.annotation.nullability.NotNullParams;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

@NotNullParams
public record AContainerSlotListener(Player player) implements ContainerListener {
    @Override
    public void slotChanged(AbstractContainerMenu container, int index, ItemStack stack) {
        if (!stack.isEmpty()/* && container.getSlot(index).container != player.getInventory()*/) {
            MinecraftForge.EVENT_BUS.post(new ContainerChangedEvent(player, container, index));
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu container, int index, int item) {

    }
}
