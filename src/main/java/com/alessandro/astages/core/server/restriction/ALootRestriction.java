package com.alessandro.astages.core.server.restriction;

import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.util.develop.Info;
import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ALootRestriction extends ARestriction<ALootRestriction, Void, ItemStack> {
    private final List<Item> restrictedItems = new ArrayList<>();
    private final List<ResourceLocation> restrictedTags = new ArrayList<>();
    private final List<String> restrictedMods = new ArrayList<>();
    private final List<Item> ignoredItems = new ArrayList<>();
    private final List<ResourceLocation> ignoredTags = new ArrayList<>();

    private final List<EntityType<?>> entities = new ArrayList<>();
    private final List<ResourceLocation> lootTables = new ArrayList<>();

    private Function<ItemStack, ItemStack> replacer;

    public ALootRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public @NotNull AttributeStore allowedAttributes() {
        return AttributeStore.builder()
                .addAttribute(Attributes.HAS_REPLACER);
    }

    @Override
    @DoNotCall
    @Info("Prefer using methods below!")
    public ALootRestriction restrict(Void unused) {
        return null;
    }

    public ALootRestriction restrictItems(Item... items) {
        restrictedItems.addAll(List.of(items));
        return this;
    }

    public ALootRestriction restrictTags(ResourceLocation... items) {
        restrictedTags.addAll(List.of(items));
        return this;
    }

    public ALootRestriction restrictMods(String... items) {
        restrictedMods.addAll(List.of(items));
        return this;
    }

    public ALootRestriction ignoredItems(Item... items) {
        ignoredItems.addAll(List.of(items));
        return this;
    }

    public ALootRestriction ignoredTags(ResourceLocation... items) {
        ignoredTags.addAll(List.of(items));
        return this;
    }

    public ALootRestriction restrictForEntities(EntityType<?>... entityTypes) {
        entities.addAll(List.of(entityTypes));
        return this;
    }

    public ALootRestriction restrictForLootTables(ResourceLocation... lootTables) {
        this.lootTables.addAll(List.of(lootTables));
        return this;
    }

    public ALootRestriction setReplacer(Function<ItemStack, ItemStack> replacer) {
        this.replacer = replacer;
        set(Attributes.HAS_REPLACER, true);
        return this;
    }

    // /give Dev chest{BlockEntityTag:{LootTable:"chests/village/village_toolsmith"}}
    // /setblock ~ ~1 ~ minecraft:chest{LootTable:"minecraft:chests/simple_dungeon"}

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        if (!ignoredItems.isEmpty() &&
                ignoredItems.stream().anyMatch(stack::is)) {
            return false;
        }

        if (!ignoredTags.isEmpty() &&
                ignoredTags.stream().anyMatch(ignoredTag -> stack.getTags().anyMatch(tag -> tag.location().equals(ignoredTag)))) {
            return false;
        }

        // Prefer mod check first!
        var registry = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (registry != null) {
            if (!restrictedMods.isEmpty() &&
                    restrictedMods.stream().anyMatch(modId -> modId.equals(registry.getNamespace()))) {
                return true;
            }
        }

        if  (!restrictedItems.isEmpty() &&
                restrictedItems.stream().anyMatch(stack::is)) {
            return true;
        }

        if (!restrictedTags.isEmpty() &&
                restrictedTags.stream().anyMatch(tag -> stack.getTags().anyMatch(t -> t.location().equals(tag)))) {
            return true;
        }

        return false;
    }

    public boolean isRestricted(ItemStack stack, @Nullable EntityType<?> entityType, @Nullable ResourceLocation lootTable) {
        if (entityType != null) {
            return entities.contains(entityType) && isRestricted(stack);
        }

        if (lootTable != null) {
            return lootTables.contains(lootTable) && isRestricted(stack);
        }

        return false;
    }

    public Function<ItemStack, ItemStack> getReplacer() {
        return replacer;
    }
}