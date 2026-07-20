package com.alessandro.astages.internal.legacy.item;

import com.alessandro.astages.AStages;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Deprecated(forRemoval = true)
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AStages.MODID);
}
