package com.alessandro.astages.core.client.restriction.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class AClientItemRestriction extends AClientBaseItemRestriction<AClientItemRestriction, Item> {
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
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        return items.contains(stack.getItem());
    }

    public List<Item> getItems() {
        return items;
    }
}
