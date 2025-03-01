package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.core.client.item.AClientModRestriction;
import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ModSyncerS2CPacket extends RestrictionSyncerPacket {
    private final String modId;
    private final List<Item> ignoredItems;
    // private final List<TagKey<Item>> tagsIgnored;
    private final List<ResourceLocation> ignoredTags;
    private final boolean hideInJei;

    public ModSyncerS2CPacket(String id, String stage, String modId, List<Item> ignoredItems, List<ResourceLocation> ignoredTags, boolean hideInJei) {
        super(id, stage);
        this.modId = modId;
        this.ignoredItems = ignoredItems;
        this.ignoredTags = ignoredTags;
        this.hideInJei = hideInJei;
    }

    public ModSyncerS2CPacket(FriendlyByteBuf buf) {
        super(buf);
        modId = buf.readUtf();
        ignoredItems = buf.readList(r -> r.readRegistryIdUnsafe(ForgeRegistries.ITEMS));
        ignoredTags = buf.readList(FriendlyByteBuf::readResourceLocation);
        hideInJei = buf.readBoolean();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeUtf(modId);
        buf.writeCollection(ignoredItems, (w, item) -> w.writeRegistryIdUnsafe(ForgeRegistries.ITEMS, item));
        buf.writeCollection(ignoredTags, FriendlyByteBuf::writeResourceLocation);
        buf.writeBoolean(hideInJei);
    }

    @Override
    public void handle() {
//        var restriction = new AClientModRestriction(getId(), getStage(), modId, ignoredItems, ignoredTags);
//        AClientRestrictionManager.NEW_ITEM_INSTANCE.addRestriction(getStage(), restriction);
        var restriction = new AClientModRestriction(getId(), getStage()).setHideInJei(hideInJei);
        restriction.restrict(modId);
        restriction.ignoreItems(ignoredItems);
        restriction.ignoreTags(ignoredTags);
        AClientRestrictionManager.NEW_ITEM_INSTANCE.addRestriction(restriction);
    }
}
