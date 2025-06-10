package com.alessandro.astages.networking.packet.ore;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.AClientOreRestriction;
import com.alessandro.astages.core.server.restriction.AOreRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class OreSyncerS2CPacket extends RestrictionSyncerPacket {
    private final BlockState original;
    private final BlockState replacement;

    public OreSyncerS2CPacket(AOreRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getOriginal(), restriction.getReplacement());
    }

    public OreSyncerS2CPacket(String id, String stage, BlockState original, BlockState replacement) {
        super(id, stage);
        this.original = original;
        this.replacement = replacement;
    }

    public OreSyncerS2CPacket(FriendlyByteBuf buf) {
        super(buf);
        original = buf.readJsonWithCodec(BlockState.CODEC);
        replacement = buf.readJsonWithCodec(BlockState.CODEC);
    }

    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeJsonWithCodec(BlockState.CODEC, original);
        buf.writeJsonWithCodec(BlockState.CODEC, replacement);
    }

    @Override
    public void handle() {
        var restriction = new AClientOreRestriction(getId(), getStage())
                .restrict(new OreWrapper(original, replacement));

        AClientRestrictionManager.ORE_INSTANCE.addRestriction(restriction);
    }
}
