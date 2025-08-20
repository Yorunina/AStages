package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.item.AClientItemRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemRestriction;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.annotations.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@NotNullParams
public class ItemSyncerS2CPacket extends ABaseItemSyncerPacket {
    private final List<Item> items;

    public ItemSyncerS2CPacket(AItemRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getItems(),
                restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    public ItemSyncerS2CPacket(String id, String stage, List<Item> items, boolean renderItemName, boolean hideTooltip, boolean hideInJei) {
        super(id, stage, renderItemName, hideTooltip, hideInJei);
        this.items = items;
    }

    public ItemSyncerS2CPacket(FriendlyByteBuf buf) {
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
