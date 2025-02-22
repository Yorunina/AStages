package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.core.client.item.AClientItemRestriction;
import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class ItemSyncerS2CPacket extends RestrictionSyncerPacket {
    private final List<Item> items;

    public ItemSyncerS2CPacket(String id, String stage, List<Item> items) {
        super(id, stage);
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
//        var restriction = new AClientItemRestriction(getId(), getStage(), items);
//        AClientRestrictionManager.NEW_ITEM_INSTANCE.addRestriction(getStage(), restriction);
        var restriction = new AClientItemRestriction(getId(), getStage());
        for (var item : items) {
            restriction.restrict(item);
        }
        AClientRestrictionManager.NEW_ITEM_INSTANCE.addRestriction(restriction);
    }
}
