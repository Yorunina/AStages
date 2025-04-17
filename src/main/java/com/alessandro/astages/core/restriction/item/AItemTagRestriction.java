package com.alessandro.astages.core.restriction.item;

import com.alessandro.astages.util.develop.Info;
import com.alessandro.astages.util.develop.UnderDevelopment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AItemTagRestriction extends ABaseItemRestriction<AItemTagRestriction, ResourceLocation> {
    private ResourceLocation tag;
    private final List<Item> ignoredItems = new ArrayList<>();

    public AItemTagRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AItemTagRestriction restrict(ResourceLocation tag) {
        this.tag = tag;
        return this;
    }

    @UnderDevelopment
    @Info("Probably a mismatch between anyMatch and noneMatch")
    @Override
    public boolean isRestricted(@NotNull ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        return !ignoredItems.contains(stack.getItem()) && stack.getTags().anyMatch(t -> t.location().equals(tag));
    }

    @SuppressWarnings("unused")
    public void ignoreItems(Item... items) {
        ignoredItems.addAll(List.of(items));
    }

    public ResourceLocation getTag() {
        return tag;
    }

    public List<Item> getIgnoredItems() {
        return ignoredItems;
    }
}
