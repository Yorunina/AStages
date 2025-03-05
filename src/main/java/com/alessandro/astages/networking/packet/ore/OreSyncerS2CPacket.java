package com.alessandro.astages.networking.packet.ore;

import com.alessandro.astages.core.client.AClientOreRestriction;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.restriction.AOreRestriction;
import com.alessandro.astages.event.custom.actions.ClientOreUpdateEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class OreSyncerS2CPacket {
    private final String id;
    private final String stage;
    private final BlockState original;
    private final BlockState replacement;
    private final boolean requestReload;

    public OreSyncerS2CPacket(AOreRestriction restriction, boolean requestReload) {
        this(restriction.getId(), restriction.getStage(), restriction.getOriginal(), restriction.getReplacement(), requestReload);
    }

    public OreSyncerS2CPacket(String id, String stage, BlockState original, BlockState replacement, boolean requestReload) {
        this.id = id;
        this.stage = stage;
        this.original = original;
        this.replacement = replacement;
        this.requestReload = requestReload;
    }

    public OreSyncerS2CPacket(FriendlyByteBuf buf) {
        id = buf.readUtf();
        stage = buf.readUtf();
        original = buf.readJsonWithCodec(BlockState.CODEC);
        replacement = buf.readJsonWithCodec(BlockState.CODEC);
        requestReload = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeUtf(stage);
        buf.writeJsonWithCodec(BlockState.CODEC, original);
        buf.writeJsonWithCodec(BlockState.CODEC, replacement);
        buf.writeBoolean(requestReload);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var restriction = new AClientOreRestriction(id, stage, original, replacement);
            AClientRestrictionManager.ORE_INSTANCE.addRestriction(stage, restriction);

            if (requestReload) {
                MinecraftForge.EVENT_BUS.post(new ClientOreUpdateEvent());
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
