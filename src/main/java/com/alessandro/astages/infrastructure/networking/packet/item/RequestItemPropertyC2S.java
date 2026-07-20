package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.infrastructure.networking.Networking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@NotNullParams
public class RequestItemPropertyC2S implements AStagesPacket {
    private static final Function<String, RuntimeException> EXCEPTION = id -> new RuntimeException("Illegal identifier synchronization: " + id + " de-synchronized between server and client!");
    private static final Function<String, RuntimeException> NULL_EXCEPTION = id -> new NullPointerException("Illegal null synchronization: " + id + " not found on server!");

    private final String id;
    private final String stage;
    private final ItemStack stack;

    public RequestItemPropertyC2S(String id, String stage, ItemStack stack) {
        this.id = id;
        this.stage = stage;
        this.stack = stack;
    }

    public RequestItemPropertyC2S(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.stage = buf.readUtf();
        this.stack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
        buf.writeItem(stack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON SERVER!
            var serverRestriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(id);

            if (serverRestriction != null) {
                if (!Objects.equals(serverRestriction.getId(), id)) { throw EXCEPTION.apply(id); }
                if (!Objects.equals(serverRestriction.getStage(), stage)) { throw EXCEPTION.apply(id); }

                Networking.sendToPlayer(ctx.get().getSender(), new ReplyItemPropertyS2C(id, stage, stack,
                    serverRestriction.get(Attributes.Item.HIDDEN_NAME).apply(stack),
                    serverRestriction.get(Attributes.Item.JADE_ITEM_MESSAGE).apply(stack),
                    serverRestriction.get(Attributes.Item.JADE_BLOCK_MESSAGE).apply(stack)
                ));
            } else {
                throw NULL_EXCEPTION.apply(id);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
