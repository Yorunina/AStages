package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.store.Attributes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class RequestItemPropertyC2SPacket {
    private static final Function<String, RuntimeException> EXCEPTION = id -> new RuntimeException("Illegal identifier synchronization: " + id + " de-synchronized between server and client!");
    private static final Function<String, RuntimeException> NULL_EXCEPTION = id -> new NullPointerException("Illegal null synchronization: " + id + " not found on server!");

    private final String id;
    private final String stage;
    private final ItemStack stack;

    public RequestItemPropertyC2SPacket(String id, String stage, ItemStack stack) {
        this.id = id;
        this.stage = stage;
        this.stack = stack;
    }

    public RequestItemPropertyC2SPacket(@NotNull FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.stage = buf.readUtf();
        this.stack = buf.readItem();
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
        buf.writeItem(stack);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON SERVER!
            var serverRestriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(id);

            if (serverRestriction != null) {
                if (!Objects.equals(serverRestriction.getId(), id)) { throw EXCEPTION.apply(id); }
                if (!Objects.equals(serverRestriction.getStage(), stage)) { throw EXCEPTION.apply(id); }

                ModNetworking.sendToPlayer(new ItemPropertySyncerS2CPacket(id, stage, stack,
                    serverRestriction.get(Attributes.RENDERING_NAME),
                    serverRestriction.get(Attributes.HIDING_TOOLTIP),
                    serverRestriction.get(Attributes.Item.HIDDEN_NAME).apply(stack),
                    serverRestriction.get(Attributes.Item.JADE_ITEM_MESSAGE).apply(stack),
                    serverRestriction.get(Attributes.Item.JADE_BLOCK_MESSAGE).apply(stack)
                ), ctx.get().getSender());
            } else {
                throw NULL_EXCEPTION.apply(id);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
