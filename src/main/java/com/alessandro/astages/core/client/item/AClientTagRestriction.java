package com.alessandro.astages.core.client.item;

import com.alessandro.astages.store.AClientRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AClientTagRestriction extends AClientRestriction<AClientTagRestriction, ResourceLocation, ItemStack> {
    private ResourceLocation tag;
    private final List<Item> ignoredItems = new ArrayList<>();

    public AClientTagRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AClientTagRestriction restrict(ResourceLocation tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isRestricted(@NotNull ItemStack stack) {
        return !ignoredItems.contains(stack.getItem()) && stack.getTags().anyMatch(t -> t.location().equals(tag));
    }

    @SuppressWarnings("unused")
    public void ignoreItems(Item... items) {
        ignoredItems.addAll(List.of(items));
    }

    public void ignoreItems(List<Item> items) {
        ignoredItems.addAll(items);
    }

    public ResourceLocation getTag() {
        return tag;
    }

    public List<Item> getIgnoredItems() {
        return ignoredItems;
    }
}
