package com.alessandro.astages.engine.client.restriction.item;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.AClientRestrictionManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@NotNullParams
public class AClientItemModRestriction extends AClientBaseItemRestriction<AClientItemModRestriction, String> {
    private final Set<String> modIds = new HashSet<>();
    private final Set<Item> ignoredItems = new HashSet<>();
    private final Set<TagKey<Item>> ignoredTags = new HashSet<>();

    public AClientItemModRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withPlugin(AClientRestrictionManager.ATTACHED_ATTRIBUTES, AClientItemModRestriction.class)
            .build();
    }

    @Override
    public AClientItemModRestriction restrict(String modId) {
        modIds.add(modId);
        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        var registry = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (registry != null) {
            return !ignoredItems.contains(stack.getItem()) &&
                ignoredTags.stream().noneMatch(stack::is) &&
                modIds.contains(registry.getNamespace());
        }

        return false;
    }

    public AClientItemModRestriction ignoreItems(Set<Item> items) {
        ignoredItems.addAll(items);
        return this;
    }

    public AClientItemModRestriction ignoreTags(Set<TagKey<Item>> tags) {
        ignoredTags.addAll(tags);
        return this;
    }

    public Set<String> getModIds() {
        return modIds;
    }

    public Set<Item> getIgnoredItems() {
        return ignoredItems;
    }

    public Set<TagKey<Item>> getIgnoredTags() {
        return ignoredTags;
    }
}
