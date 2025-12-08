package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.item.AClientItemModRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemModRestriction;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@NotNullParams
public class ItemModSyncerS2CPacket extends ABaseItemSyncerPacket {
    private final String modId;
    private final List<Item> ignoredItems;
    private final List<ResourceLocation> ignoredTags;

    public ItemModSyncerS2CPacket(AItemModRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModId(), restriction.getIgnoredItems(), restriction.getIgnoredTags(),
                restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    public ItemModSyncerS2CPacket(String id, String stage, String modId, List<Item> ignoredItems, List<ResourceLocation> ignoredTags, boolean renderItemName, boolean hideTooltip, boolean hideInJei) {
        super(id, stage, renderItemName, hideTooltip, hideInJei);
        this.modId = modId;
        this.ignoredItems = ignoredItems;
        this.ignoredTags = ignoredTags;
    }

    public ItemModSyncerS2CPacket(FriendlyByteBuf buf) {
        super(buf);
        modId = buf.readUtf();
        ignoredItems = buf.readList(r -> r.readRegistryIdUnsafe(ForgeRegistries.ITEMS));
        ignoredTags = buf.readList(FriendlyByteBuf::readResourceLocation);
    }


    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeUtf(modId);
        buf.writeCollection(ignoredItems, (w, item) -> w.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, item));
        buf.writeCollection(ignoredTags, FriendlyByteBuf::writeResourceLocation);
    }

    @Override
    public void handle() {
        var restriction = new AClientItemModRestriction(getId(), getStage())
                .set(Attributes.RENDERING_NAME, isRenderItemName())
                .set(Attributes.HIDING_TOOLTIP, isHideTooltip())
                .set(Attributes.HIDING_JEI, isHideInJei())
                .restrict(modId)
                .ignoreItems(ignoredItems)
                .ignoreTags(ignoredTags);

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }
}
