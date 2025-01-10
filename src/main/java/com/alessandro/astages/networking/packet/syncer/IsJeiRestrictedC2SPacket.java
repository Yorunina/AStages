package com.alessandro.astages.networking.packet.syncer;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class IsJeiRestrictedC2SPacket {
    private final ItemStack stack;
    private final boolean requestReload;

    public IsJeiRestrictedC2SPacket(ItemStack stack, boolean requestReload) {
        this.stack = stack;
        this.requestReload = requestReload;
    }

    public IsJeiRestrictedC2SPacket(@NotNull FriendlyByteBuf buf) {
        stack = buf.readItem();
        requestReload = buf.readBoolean();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeBoolean(requestReload);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON SERVER!
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(Objects.requireNonNull(ctx.get().getSender()), stack);

            if (restriction != null && restriction.isEnabled(Attributes.HIDING_JEI)) {
                ModNetworking.sendToPlayer(new JeiIsRestrictedS2CPacket(stack), ctx.get().getSender());
            }

            if (requestReload) {
                ModNetworking.sendToPlayer(new JeiSyncerS2CPacket(), ctx.get().getSender());
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
