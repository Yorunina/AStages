package com.alessandro.astages.networking.packet;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.client.AClientItemManager;
import com.alessandro.astages.core.client.AClientItemRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemClientDataS2CPacket {
    private final String stage;
    private final Item item;
    private final boolean renderItemName;
    private final boolean hideTooltip;
    private final boolean hideInJEI;

    public ItemClientDataS2CPacket(String stage, Item item, boolean renderItemName, boolean hideTooltip, boolean hideInJEI) {
        this.stage = stage;
        this.item = item;
        this.renderItemName = renderItemName;
        this.hideTooltip = hideTooltip;
        this.hideInJEI = hideInJEI;
    }

    public ItemClientDataS2CPacket(@NotNull FriendlyByteBuf buf) {
        stage = buf.readUtf();
        item = buf.readItem().getItem();
        renderItemName = buf.readBoolean();
        hideTooltip = buf.readBoolean();
        hideInJEI = buf.readBoolean();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(stage);
        buf.writeItem(new ItemStack(item));
        buf.writeBoolean(renderItemName);
        buf.writeBoolean(hideTooltip);
        buf.writeBoolean(hideInJEI);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT
//            var restriction = new AClientItemRestriction("client/" + AClientRestrictionManager.getId(), stage, new ItemStack(item), renderItemName, hideTooltip, hideInJEI);
//            AClientRestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);
        });

        ctx.get().setPacketHandled(true);
    }
}
