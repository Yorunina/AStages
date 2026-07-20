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
                restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    public SyncItemS2C(String id, String stage, List<Item> items, boolean renderItemName, boolean hideTooltip, boolean hideInJei) {
        super(id, stage, renderItemName, hideTooltip, hideInJei);
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
                .set(Attributes.RENDERING_NAME, isRenderItemName())
                .set(Attributes.HIDING_TOOLTIP, isHideTooltip())
                .set(Attributes.HIDING_JEI, isHideInJei());

        for (var item : items) {
            restriction.restrict(item);
        }

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }
}
