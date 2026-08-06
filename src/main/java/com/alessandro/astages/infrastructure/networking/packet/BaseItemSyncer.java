package com.alessandro.astages.infrastructure.networking.packet;

import com.alessandro.astages.api.nullability.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;

@NotNullParams
public abstract class BaseItemSyncer extends BaseRestrictionSyncer {
    private final boolean hideInRecipeViewer;
    private final boolean showActionBarName;
    private final boolean showTooltipName;
    private final boolean showRecipeViewerName;
    private final boolean showJadeItemName;
    private final boolean showJadeBlockName;

    public BaseItemSyncer(String id, String stage, boolean hideInRecipeViewer, boolean showActionBarName, boolean showTooltipName, boolean showRecipeViewerName, boolean showJadeItemName, boolean showJadeBlockName) {
        super(id, stage);
        this.hideInRecipeViewer = hideInRecipeViewer;
        this.showActionBarName = showActionBarName;
        this.showTooltipName = showTooltipName;
        this.showRecipeViewerName = showRecipeViewerName;
        this.showJadeItemName = showJadeItemName;
        this.showJadeBlockName = showJadeBlockName;
    }

    public BaseItemSyncer(FriendlyByteBuf buf) {
        super(buf);
        hideInRecipeViewer = buf.readBoolean();
        showActionBarName = buf.readBoolean();
        showTooltipName = buf.readBoolean();
        showRecipeViewerName = buf.readBoolean();
        showJadeItemName = buf.readBoolean();
        showJadeBlockName = buf.readBoolean();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(hideInRecipeViewer);
        buf.writeBoolean(showActionBarName);
        buf.writeBoolean(showTooltipName);
        buf.writeBoolean(showRecipeViewerName);
        buf.writeBoolean(showJadeItemName);
        buf.writeBoolean(showJadeBlockName);
    }
    
    public boolean isHideInRecipeViewer() {
        return hideInRecipeViewer;
    }

    public boolean isShowActionBarName() {
        return showActionBarName;
    }

    public boolean isShowTooltipName() {
        return showTooltipName;
    }

    public boolean isShowRecipeViewerName() {
        return showRecipeViewerName;
    }

    public boolean isShowJadeItemName() {
        return showJadeItemName;
    }

    public boolean isShowJadeBlockName() {
        return showJadeBlockName;
    }
}
