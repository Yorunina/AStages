package com.alessandro.astages.core.server.restriction.item;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.ALootRestriction;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.item.ItemSyncerS2CPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class AItemRestriction extends ABaseItemRestriction<AItemRestriction, Item> {
    private final List<Item> items = new ArrayList<>();

    public AItemRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AItemRestriction restrict(Item item) {
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

    @Override
    public void markAsDirty() {
        ModNetworking.sendTo(null, new ItemSyncerS2CPacket(this));
        super.markAsDirty();
    }

    @Override
    public AItemRestriction associateLootRestriction(String id) {
        var restriction = new ALootRestriction(id, getStage()).applyForEveryLootTableAndDrop(true);
        for (var item : items) { restriction.restrictItems(item); }
        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return this;
    }
}
