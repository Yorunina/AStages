package com.alessandro.astages.networking.packet.ud;

import com.alessandro.astages.capability.BlockStage;
import com.alessandro.astages.core.client.AClientOreRestriction;
import com.alessandro.astages.core.client.AClientRestrictionManager;
import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OreSyncerS2CPacket {
    private final String id;
    private final String stage;
    private final BlockState original;
    private final BlockState replacement;

    public OreSyncerS2CPacket(String id, String stage, BlockState original, BlockState replacement) {
        this.id = id;
        this.stage = stage;
        this.original = original;
        this.replacement = replacement;
    }

    public OreSyncerS2CPacket(@NotNull FriendlyByteBuf buf) {
        id = buf.readUtf();
        stage = buf.readUtf();
        original = buf.readJsonWithCodec(BlockState.CODEC);
        replacement = buf.readJsonWithCodec(BlockState.CODEC);
    }

    public void toBytes(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
        buf.writeJsonWithCodec(BlockState.CODEC, original);
        buf.writeJsonWithCodec(BlockState.CODEC, replacement);
    }

    public void handle(@NotNull Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var restriction = new AClientOreRestriction(id, stage, original, replacement);
            AClientRestrictionManager.ORE_INSTANCE.addRestriction(stage, restriction);

            MinecraftForge.EVENT_BUS.post(new ClientOreUpdateEvent());
        });

        ctx.get().setPacketHandled(true);
    }
}
