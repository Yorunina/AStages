package com.alessandro.astages.core.client.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.store.AClientRestriction;
import com.alessandro.astages.util.develop.Info;
import com.alessandro.astages.util.develop.UnderDevelopment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AClientItemManager {
    private final List<AClientRestriction<?, ?, ItemStack>> restrictions = new ArrayList<>();
    private final List<AClientItemRestriction> items = new ArrayList<>();
    private final List<AClientTagRestriction> tags = new ArrayList<>();
    private final List<AClientModRestriction> mods = new ArrayList<>();

    public List<AClientItemRestriction> getItemRestrictions() {
        return items;
    }

    public List<AClientTagRestriction> getTagRestrictions() {
        return tags;
    }

    public List<AClientModRestriction> getModRestrictions() {
        return mods;
    }

    public List<AClientRestriction<?, ?, ItemStack>> getRestrictions() {
        return restrictions;
    }

    public void reloadBeforeScripts() {
        restrictions.clear();
        items.clear();
        tags.clear();
        mods.clear();
    }

    public void addRestriction(AClientItemRestriction restriction) {
        commonAddOperations(restriction);
        items.add(restriction);
    }

    public void addRestriction(AClientTagRestriction restriction) {
        commonAddOperations(restriction);
        tags.add(restriction);
    }

    public void addRestriction(AClientModRestriction restriction) {
        commonAddOperations(restriction);
        mods.add(restriction);
        AStages.LOGGER.debug(mods.toString());
    }

    private void commonAddOperations(AClientRestriction<?, ?, ItemStack> restriction) {
        restrictions.add(restriction);
    }

    @Info("Missing ignoring!")
    @UnderDevelopment
    public Set<String> getStagesForStack(@NotNull ItemStack stack) {
        Set<String> toReturn = new HashSet<>();

        restrictions.forEach(restriction -> {
            if (restriction.isRestricted(stack)) { toReturn.add(restriction.getStage()); }
        });

        return toReturn;
    }

    @UnderDevelopment
    public Set<String> getStagesForResourceLocation(ResourceLocation resourceLocation) {
        Set<String> toReturn = new HashSet<>();

        mods.forEach(restriction -> {
            if (Objects.equals(restriction.getModId(), resourceLocation.getNamespace())) {
                toReturn.add(restriction.getStage());
            }
        });

        return toReturn;
    }
}
