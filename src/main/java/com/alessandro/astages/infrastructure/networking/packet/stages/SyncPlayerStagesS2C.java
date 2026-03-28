package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.util.ASetUtils;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.event.sync.ClientSynchronizeStagesEvent;
import com.alessandro.astages.infrastructure.networking.packet.BaseStageSyncer;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Set;

@NotNullParams
public class SyncPlayerStagesS2C extends BaseStageSyncer {
    public SyncPlayerStagesS2C(Set<String> stages, AOperation operation) {
        super(stages, operation);
    }

    public SyncPlayerStagesS2C(FriendlyByteBuf buf) {
        super(buf);
    }

    public void handle() {
        // HERE WE ARE ON CLIENT!
        switch (getOperation()) {
            case ADD -> AStagesClientUtils.addStage(AClientHolder.player(), ASetUtils.getOnlyElement(getStages()));
            case ADD_ALL -> AStagesClientUtils.addStages(AClientHolder.player(), getStages());
            case REMOVE -> AStagesClientUtils.removeStage(AClientHolder.player(), ASetUtils.getOnlyElement(getStages()));
            case REMOVE_ALL -> AStagesClientUtils.removeStages(AClientHolder.player(), getStages());
            case LOGIN -> AStagesClientUtils.setStages(AClientHolder.player(), getStages());
        }

        ALoader.EVENT_BUS.post(new ClientSynchronizeStagesEvent(getStages(), getOperation()));
    }
}
