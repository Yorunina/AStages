package com.alessandro.astages.core.client.item;

import com.alessandro.astages.store.AClientRestriction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AClientItemRestriction extends AClientRestriction<AClientItemRestriction, Item, ItemStack> {
    private final List<Item> items = new ArrayList<>();

    public AClientItemRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AClientItemRestriction restrict(Item item) {
        items.add(item);

        return this;
    }

    @Override
    public boolean isRestricted(@NotNull ItemStack stack) {
        return items.contains(stack.getItem());
    }

    public List<Item> getItems() {
        return items;
    }
}
