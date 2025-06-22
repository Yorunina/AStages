package com.alessandro.astages.core.client.item;

import com.alessandro.astages.store.client.AClientRestriction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AClientModRestriction extends AClientRestriction<AClientModRestriction, String, ItemStack> {
    private String modId;
    private final List<Item> ignoredItems = new ArrayList<>();
    private final List<ResourceLocation> ignoredTags = new ArrayList<>();

    public AClientModRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AClientModRestriction restrict(String modId) {
        this.modId = modId;
        return this;
    }

    @Override
    public boolean isRestricted(@NotNull ItemStack stack) {
        var registry = BuiltInRegistries.ITEM.getKey(stack.getItem());

        return !ignoredItems.contains(stack.getItem()) &&
            modId.equals(registry.getNamespace()) &&
            stack.getTags().noneMatch(t -> ignoredTags.contains(t.location()));
    }

    public void ignoreItems(Item... items) {
        ignoredItems.addAll(List.of(items));
    }

    public void ignoreItems(List<Item> items) {
        ignoredItems.addAll(items);
    }

    public final void ignoreTags(ResourceLocation... items) {
        ignoredTags.addAll(List.of(items));
    }

    public final void ignoreTags(List<ResourceLocation> items) {
        ignoredTags.addAll(items);
    }

    public String getModId() {
        return modId;
    }

    public List<Item> getIgnoredItems() {
        return ignoredItems;
    }

    public List<ResourceLocation> getIgnoredTags() {
        return ignoredTags;
    }
}
