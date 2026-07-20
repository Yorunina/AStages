package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.ClientStage;
import com.alessandro.astages.engine.AClientStageManager;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.engine.store.StageAttributes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@NotNullParams
public class SyncPermanentStageS2C implements AStagesPacket {
    private final String stageKey;
    private final ItemStack stack;

    public SyncPermanentStageS2C(String stageKey, @Nullable ItemStack stack) {
        this.stageKey = stageKey;
        this.stack = stack;
    }

    public SyncPermanentStageS2C(FriendlyByteBuf buf) {
        stageKey = buf.readUtf();
        stack = buf.readNullable(FriendlyByteBuf::readItem);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(stageKey);
        buf.writeNullable(stack, ((b, itemStack) -> b.writeItemStack(itemStack, false)));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            AClientStageManager.PERMANENT_INSTANCE.addStage(new ClientStage(stageKey).set(StageAttributes.ICON, stack));
        });

        ctx.get().setPacketHandled(true);
    }
}
