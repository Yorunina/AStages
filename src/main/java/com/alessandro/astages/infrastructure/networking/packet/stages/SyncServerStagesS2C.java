package com.alessandro.astages.infrastructure.networking.packet.stages;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.util.ASetUtils;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.event.sync.ClientSynchronizeServerStagesEvent;
import com.alessandro.astages.infrastructure.networking.packet.BaseStageSyncer;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Set;

@NotNullParams
public class SyncServerStagesS2C extends BaseStageSyncer {
    public SyncServerStagesS2C(Set<String> stages, AOperation operation) {
        super(stages, operation);
    }

    public SyncServerStagesS2C(FriendlyByteBuf buf) {
        super(buf);
    }

    public void handle() {
        // HERE WE ARE ON CLIENT!
        switch (getOperation()) {
            case ADD -> AStagesClientUtils.addStage(AClientHolder.server(), ASetUtils.getOnlyElement(getStages()));
            case ADD_ALL -> AStagesClientUtils.addStages(AClientHolder.server(), getStages());
            case REMOVE -> AStagesClientUtils.removeStage(AClientHolder.server(), ASetUtils.getOnlyElement(getStages()));
            case REMOVE_ALL -> AStagesClientUtils.removeStages(AClientHolder.server(), getStages());
            case LOGIN -> AStagesClientUtils.setStages(AClientHolder.server(), getStages());
        }

        ALoader.EVENT_BUS.post(new ClientSynchronizeServerStagesEvent(getStages(), getOperation()));
    }
}
