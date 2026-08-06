package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.packet.BaseItemSyncer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@NotNullParams
public class SyncItemS2C extends BaseItemSyncer {
    private final List<Item> items;

    public SyncItemS2C(AItemRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getItems(),
            restriction.get(Attributes.HIDING_RECIPE_VIEWER), restriction.get(Attributes.SHOW_ACTION_BAR_NAME), restriction.get(Attributes.SHOW_TOOLTIP_NAME), restriction.get(Attributes.SHOW_RECIPE_VIEWER_NAME), restriction.get(Attributes.SHOW_JADE_BLOCK_NAME), restriction.get(Attributes.SHOW_JADE_BLOCK_NAME));
    }

    public SyncItemS2C(String id, String stage, List<Item> items, boolean hideInRecipeViewer, boolean showActionBarName, boolean showTooltipName, boolean showRecipeViewerName, boolean showJadeItemName, boolean showJadeBlockName) {
        super(id, stage, hideInRecipeViewer, showActionBarName, showTooltipName, showRecipeViewerName, showJadeItemName, showJadeBlockName);
        this.items = items;
    }

    public SyncItemS2C(FriendlyByteBuf buf) {
        super(buf);
        items = buf.readList(r -> r.readRegistryIdUnsafe(ForgeRegistries.ITEMS));
    }


    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeCollection(items, (w, item) -> w.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, item));
    }

    @Override
    public void handle() {
        var restriction = new AClientItemRestriction(getId(), getStage())
            .set(Attributes.HIDING_RECIPE_VIEWER, isHideInRecipeViewer())
            .set(Attributes.SHOW_ACTION_BAR_NAME, isShowActionBarName())
            .set(Attributes.SHOW_TOOLTIP_NAME, isShowTooltipName())
            .set(Attributes.SHOW_RECIPE_VIEWER_NAME, isShowRecipeViewerName())
            .set(Attributes.SHOW_JADE_ITEM_NAME, isShowJadeItemName())
            .set(Attributes.SHOW_JADE_BLOCK_NAME, isShowJadeBlockName());

        for (var item : items) {
            restriction.restrict(item);
        }

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }
}
