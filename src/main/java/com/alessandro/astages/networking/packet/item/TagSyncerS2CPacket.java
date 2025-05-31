package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.item.AClientTagRestriction;
import com.alessandro.astages.core.restriction.item.AItemTagRestriction;
import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class TagSyncerS2CPacket extends RestrictionSyncerPacket {
    private final ResourceLocation tag;
    private final List<Item> ignoredItems;
    private final boolean hideInJei;

    public TagSyncerS2CPacket(AItemTagRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getTag(), restriction.getIgnoredItems(), restriction.get(Attributes.HIDING_JEI));
    }

    public TagSyncerS2CPacket(String id, String stage, ResourceLocation tag, List<Item> ignoredItems, boolean hideInJei) {
        super(id, stage);
        this.tag = tag;
        this.ignoredItems = ignoredItems;
        this.hideInJei = hideInJei;
    }

    public TagSyncerS2CPacket(FriendlyByteBuf buf) {
        super(buf);
        tag = buf.readResourceLocation();
        ignoredItems = buf.readList(r -> r.readRegistryIdUnsafe(ForgeRegistries.ITEMS));
        hideInJei = buf.readBoolean();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeResourceLocation(tag);
        buf.writeCollection(ignoredItems, (w, item) -> w.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, item));
        buf.writeBoolean(hideInJei);
    }

    @Override
    public void handle() {
        var restriction = new AClientTagRestriction(getId(), getStage()).setHideInJei(hideInJei);
        restriction.restrict(tag);
        restriction.ignoreItems(ignoredItems);
        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }
}
