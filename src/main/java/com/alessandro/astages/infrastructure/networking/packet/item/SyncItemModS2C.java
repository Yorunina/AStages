package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemModRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemModRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.packet.BaseItemSyncer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

@NotNullParams
public class SyncItemModS2C extends BaseItemSyncer {
    private final Set<String> modIds;
    private final Set<Item> ignoredItems;
    private final Set<TagKey<Item>> ignoredTags;

    public SyncItemModS2C(AItemModRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModIds(), restriction.getIgnoredItems(), restriction.getIgnoredTags(),
                restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    public SyncItemModS2C(String id, String stage, Set<String> modIds, Set<Item> ignoredItems, Set<TagKey<Item>> ignoredTags, boolean renderItemName, boolean hideTooltip, boolean hideInJei) {
        super(id, stage, renderItemName, hideTooltip, hideInJei);
        this.modIds = modIds;
        this.ignoredItems = ignoredItems;
        this.ignoredTags = ignoredTags;
    }

    public SyncItemModS2C(FriendlyByteBuf buf) {
        super(buf);
        modIds = buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf);
        ignoredItems = buf.readCollection(HashSet::new, r -> r.readRegistryIdUnsafe(ForgeRegistries.ITEMS));
        ignoredTags = buf.readCollection(HashSet::new, r -> TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), r.readResourceLocation()));
    }


    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeCollection(modIds, FriendlyByteBuf::writeUtf);
        buf.writeCollection(ignoredItems, (w, item) -> w.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, item));
        buf.writeCollection(ignoredTags, (w, tag) -> w.writeResourceLocation(tag.location()));
    }

    @Override
    public void handle() {
        var restriction = new AClientItemModRestriction(getId(), getStage())
                .set(Attributes.RENDERING_NAME, isRenderItemName())
                .set(Attributes.HIDING_TOOLTIP, isHideTooltip())
                .set(Attributes.HIDING_JEI, isHideInJei())
                .ignoreItems(ignoredItems)
                .ignoreTags(ignoredTags);

        for (var modId : modIds) { restriction.restrict(modId); }

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }
}
