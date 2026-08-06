package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemPredicateRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.packet.BaseItemSyncer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public class SyncItemPredicateS2C extends BaseItemSyncer {
    private final ResourceLocation modelId;

    public SyncItemPredicateS2C(AItemPredicateRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModelId(),
            restriction.get(Attributes.HIDING_RECIPE_VIEWER), restriction.get(Attributes.SHOW_ACTION_BAR_NAME), restriction.get(Attributes.SHOW_TOOLTIP_NAME), restriction.get(Attributes.SHOW_RECIPE_VIEWER_NAME), restriction.get(Attributes.SHOW_JADE_BLOCK_NAME), restriction.get(Attributes.SHOW_JADE_BLOCK_NAME));
    }

    public SyncItemPredicateS2C(String id, String stage, ResourceLocation modelId, boolean hideInRecipeViewer, boolean showActionBarName, boolean showTooltipName, boolean showRecipeViewerName, boolean showJadeItemName, boolean showJadeBlockName) {
        super(id, stage, hideInRecipeViewer, showActionBarName, showTooltipName, showRecipeViewerName, showJadeItemName, showJadeBlockName);
        this.modelId = modelId;
    }

    public SyncItemPredicateS2C(FriendlyByteBuf buf) {
        super(buf);
        modelId = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeResourceLocation(modelId);
    }

    @Override
    public void handle() {
        var restriction = new AClientItemPredicateRestriction(getId(), getStage())
            .restrict(modelId)
            .set(Attributes.HIDING_RECIPE_VIEWER, isHideInRecipeViewer())
            .set(Attributes.SHOW_ACTION_BAR_NAME, isShowActionBarName())
            .set(Attributes.SHOW_TOOLTIP_NAME, isShowTooltipName())
            .set(Attributes.SHOW_RECIPE_VIEWER_NAME, isShowRecipeViewerName())
            .set(Attributes.SHOW_JADE_ITEM_NAME, isShowJadeItemName())
            .set(Attributes.SHOW_JADE_BLOCK_NAME, isShowJadeBlockName());

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }
}
