package com.alessandro.astages.core.restriction.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AItemModRestriction extends ABaseItemRestriction<AItemModRestriction, String> {
    private String modId;
    private final List<Item> ignoredItems = new ArrayList<>();
    private final List<ResourceLocation> ignoredTags = new ArrayList<>();

    public AItemModRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AItemModRestriction restrict(String modId) {
        this.modId = modId;
        return this;
    }

    @Override
    public boolean isRestricted(@NotNull ItemStack stack) {
        var registry = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (registry != null) {
            return !ignoredItems.contains(stack.getItem()) &&
                modId.equals(registry.getNamespace()) &&
                stack.getTags().noneMatch(t -> ignoredTags.contains(t.location()));
        }

        return false;
    }

    @SuppressWarnings("unused")
    public AItemModRestriction ignoreItems(Item... items) {
        ignoredItems.addAll(List.of(items));
        return this;
    }

    @SuppressWarnings("unused")
    public AItemModRestriction ignoreTags(ResourceLocation... items) {
        ignoredTags.addAll(List.of(items));
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
