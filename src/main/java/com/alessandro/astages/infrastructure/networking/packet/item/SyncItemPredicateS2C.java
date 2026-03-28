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
                restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    public SyncItemPredicateS2C(String id, String stage, ResourceLocation modelId, boolean renderItemName, boolean hideTooltip, boolean hideInJei) {
        super(id, stage, renderItemName, hideTooltip, hideInJei);
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
                .set(Attributes.RENDERING_NAME, isRenderItemName())
                .set(Attributes.HIDING_TOOLTIP, isHideTooltip())
                .set(Attributes.HIDING_JEI, isHideInJei())
                .restrict(modelId);

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }
}
