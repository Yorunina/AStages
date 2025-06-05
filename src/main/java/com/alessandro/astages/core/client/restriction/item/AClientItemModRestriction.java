package com.alessandro.astages.core.client.restriction.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class AClientItemModRestriction extends AClientBaseItemRestriction<AClientItemModRestriction, String> {
    private String modId;
    private final List<Item> ignoredItems = new ArrayList<>();
    private final List<ResourceLocation> ignoredTags = new ArrayList<>();

    public AClientItemModRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AClientItemModRestriction restrict(String modId) {
        this.modId = modId;
        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        var registry = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (registry != null) {
            return !ignoredItems.contains(stack.getItem()) &&
                modId.equals(registry.getNamespace()) &&
                stack.getTags().noneMatch(t -> ignoredTags.contains(t.location()));
        }

        return false;
    }

    public AClientItemModRestriction ignoreItems(List<Item> items) {
        ignoredItems.addAll(items);
        return this;
    }

    public AClientItemModRestriction ignoreTags(List<ResourceLocation> tags) {
        ignoredTags.addAll(tags);
        return this;
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
