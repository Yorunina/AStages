package com.alessandro.astages.networking.packet.stages;

import com.alessandro.astages.api.ASetUtils;
import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.networking.packet.StageSyncerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.MinecraftForge;

import java.util.Set;

@NotNullParams
public class ClientStagesSyncerS2CPacket extends StageSyncerPacket {
    public ClientStagesSyncerS2CPacket(Set<String> stages, AOperation operation) {
        super(stages, operation);
    }

    public ClientStagesSyncerS2CPacket(FriendlyByteBuf buf) {
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

        MinecraftForge.EVENT_BUS.post(new ClientSynchronizeStagesEvent(getStages(), getOperation()));
    }
}
