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
            restriction.get(Attributes.HIDING_RECIPE_VIEWER), restriction.get(Attributes.SHOW_ACTION_BAR_NAME), restriction.get(Attributes.SHOW_TOOLTIP_NAME), restriction.get(Attributes.SHOW_RECIPE_VIEWER_NAME), restriction.get(Attributes.SHOW_JADE_BLOCK_NAME), restriction.get(Attributes.SHOW_JADE_BLOCK_NAME));
    }

    public SyncItemModS2C(String id, String stage, Set<String> modIds, Set<Item> ignoredItems, Set<TagKey<Item>> ignoredTags, boolean hideInRecipeViewer, boolean showActionBarName, boolean showTooltipName, boolean showRecipeViewerName, boolean showJadeItemName, boolean showJadeBlockName) {
        super(id, stage, hideInRecipeViewer, showActionBarName, showTooltipName, showRecipeViewerName, showJadeItemName, showJadeBlockName);
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
            .ignoreItems(ignoredItems)
            .ignoreTags(ignoredTags)
            .set(Attributes.HIDING_RECIPE_VIEWER, isHideInRecipeViewer())
            .set(Attributes.SHOW_ACTION_BAR_NAME, isShowActionBarName())
            .set(Attributes.SHOW_TOOLTIP_NAME, isShowTooltipName())
            .set(Attributes.SHOW_RECIPE_VIEWER_NAME, isShowRecipeViewerName())
            .set(Attributes.SHOW_JADE_ITEM_NAME, isShowJadeItemName())
            .set(Attributes.SHOW_JADE_BLOCK_NAME, isShowJadeBlockName());

        for (var modId : modIds) { restriction.restrict(modId); }

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }
}
