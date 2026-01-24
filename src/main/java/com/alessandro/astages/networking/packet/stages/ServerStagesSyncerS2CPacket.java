package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.ASetUtils;
import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.event.custom.ClientSynchronizeServerStagesEvent;
import com.alessandro.astages.networking.packet.StageSyncerPacket;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Set;

@NotNullParams
public class ServerStagesSyncerS2CPacket extends StageSyncerPacket {
    public ServerStagesSyncerS2CPacket(Set<String> stages, AOperation operation) {
        super(stages, operation);
    }

    public ServerStagesSyncerS2CPacket(FriendlyByteBuf buf) {
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
