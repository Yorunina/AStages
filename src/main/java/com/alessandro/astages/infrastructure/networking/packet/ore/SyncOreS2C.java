package com.alessandro.astages.infrastructure.networking.packet.ore;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.wrapper.OreWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.AClientOreRestriction;
import com.alessandro.astages.engine.server.restriction.AOreRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.packet.BaseRestrictionSyncer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class SyncOreS2C extends BaseRestrictionSyncer {
    private final BlockState original;
    private final BlockState replacement;
    private final boolean stageAllBlockStates;

    public SyncOreS2C(AOreRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getOriginal(), restriction.getReplacement(), restriction.get(Attributes.MATCH_ALL_BLOCK_STATES));
    }

    public SyncOreS2C(String id, String stage, BlockState original, BlockState replacement, boolean stageAllBlockStates) {
        super(id, stage);
        this.original = original;
        this.replacement = replacement;
        this.stageAllBlockStates = stageAllBlockStates;
    }

    public SyncOreS2C(FriendlyByteBuf buf) {
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
                .set(Attributes.MATCH_ALL_BLOCK_STATES, stageAllBlockStates);

        AClientRestrictionManager.ORE_INSTANCE.addRestriction(restriction);
    }
}
