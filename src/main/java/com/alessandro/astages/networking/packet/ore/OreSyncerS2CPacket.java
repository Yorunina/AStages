package com.alessandro.astages.networking.packet.ore;

import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.AClientOreRestriction;
import com.alessandro.astages.core.server.restriction.AOreRestriction;
import com.alessandro.astages.core.wrapper.OreWrapper;
import com.alessandro.astages.networking.packet.RestrictionSyncerPacket;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.annotations.NotNullParams;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class OreSyncerS2CPacket extends RestrictionSyncerPacket {
    private final BlockState original;
    private final BlockState replacement;
    private final boolean stageAllBlockStates;

    public OreSyncerS2CPacket(AOreRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getOriginal(), restriction.getReplacement(), restriction.get(Attributes.STAGE_ALL_BLOCK_STATES));
    }

    public OreSyncerS2CPacket(String id, String stage, BlockState original, BlockState replacement, boolean stageAllBlockStates) {
        super(id, stage);
        this.original = original;
        this.replacement = replacement;
        this.stageAllBlockStates = stageAllBlockStates;
    }

    public OreSyncerS2CPacket(FriendlyByteBuf buf) {
        super(buf);
        original = buf.readJsonWithCodec(BlockState.CODEC);
        replacement = buf.readJsonWithCodec(BlockState.CODEC);
        stageAllBlockStates = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeJsonWithCodec(BlockState.CODEC, original);
        buf.writeJsonWithCodec(BlockState.CODEC, replacement);
        buf.writeBoolean(stageAllBlockStates);
    }

    @Override
    public void handle() {
        var restriction = new AClientOreRestriction(getId(), getStage())
                .restrict(new OreWrapper(original, replacement))
                .set(Attributes.STAGE_ALL_BLOCK_STATES, stageAllBlockStates);

        AClientRestrictionManager.ORE_INSTANCE.addRestriction(restriction);
    }
}
