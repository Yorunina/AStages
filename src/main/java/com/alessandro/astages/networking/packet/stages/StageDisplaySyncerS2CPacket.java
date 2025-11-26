package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.ClientStage;
import com.alessandro.astages.core.AClientStageManager;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@NotNullParams
public class StageDisplaySyncerS2CPacket implements AStagesPacket {
    private final String stageKey;
    private final ItemStack stack;

    public StageDisplaySyncerS2CPacket(String stageKey, ItemStack stack) {
        this.stageKey = stageKey;
        this.stack = stack;
    }

    public StageDisplaySyncerS2CPacket(FriendlyByteBuf buf) {
        stageKey = buf.readUtf();
        stack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(stageKey);
        buf.writeItemStack(stack, false);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // HERE WE ARE ON CLIENT!
            AClientStageManager.GENERIC_INSTANCE.addStageInternal(stageKey, new ClientStage(stageKey, stack));
        });

        ctx.get().setPacketHandled(true);
    }
}
