package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.item.AClientItemPredicateRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.annotations.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@NotNullParams
public class ItemPredicateSyncerS2CPacket extends ABaseItemSyncerPacket {
    private final ResourceLocation modelId;

    public ItemPredicateSyncerS2CPacket(AItemPredicateRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModelId(),
                restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    public ItemPredicateSyncerS2CPacket(String id, String stage, ResourceLocation modelId, boolean renderItemName, boolean hideTooltip, boolean hideInJei) {
        super(id, stage, renderItemName, hideTooltip, hideInJei);
        this.modelId = modelId;
    }

    public ItemPredicateSyncerS2CPacket(FriendlyByteBuf buf) {
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
