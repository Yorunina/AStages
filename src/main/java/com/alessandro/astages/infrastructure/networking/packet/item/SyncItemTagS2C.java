package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemTagRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemTagRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.packet.BaseItemSyncer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

@NotNullParams
public class SyncItemTagS2C extends BaseItemSyncer {
    private final TagKey<Item> tag;
    private final Set<Item> ignoredItems;

    public SyncItemTagS2C(AItemTagRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getTag(), restriction.getIgnoredItems(),
                restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    public SyncItemTagS2C(String id, String stage, TagKey<Item> tag, Set<Item> ignoredItems, boolean renderItemName, boolean hideTooltip, boolean hideInJei) {
        super(id, stage, renderItemName, hideTooltip, hideInJei);
        this.tag = tag;
        this.ignoredItems = ignoredItems;
    }

    public SyncItemTagS2C(FriendlyByteBuf buf) {
        super(buf);
        tag = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), buf.readResourceLocation());
        ignoredItems = buf.readCollection(HashSet::new, r -> r.readRegistryIdUnsafe(ForgeRegistries.ITEMS));
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeResourceLocation(tag.location());
        buf.writeCollection(ignoredItems, (w, item) -> w.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, item));
    }

    @Override
    public void handle() {
        var restriction = new AClientItemTagRestriction(getId(), getStage())
                .set(Attributes.RENDERING_NAME, isRenderItemName())
                .set(Attributes.HIDING_TOOLTIP, isHideTooltip())
                .set(Attributes.HIDING_JEI, isHideInJei())
                .restrict(tag)
                .ignoreItems(ignoredItems);

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }
}
