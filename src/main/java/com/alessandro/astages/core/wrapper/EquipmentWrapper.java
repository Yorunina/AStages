package com.alessandro.astages.core.wrapper;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public record EquipmentWrapper(EquipmentSlot slot, ItemStack stack) { }
