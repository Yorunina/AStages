package com.alessandro.astages.capability;

import com.alessandro.astages.api.ASetUtils;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.ATitleUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.event.player.*;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.stages.ClientStagesSyncerS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Deprecated(forRemoval = true)
@NotNullParamsAndMethodsReturn
@AutoRegisterCapability
public class PlayerStage {
    private List<String> stages = new ArrayList<>();

    @Info("Not required, for commands only!")
    public void setChangedFor(Player player, AOperation operation, String stage) {
        setChangedFor(player, operation, stage, false);
    }

    public void setChangedFor(Player player, AOperation operation, String stage, boolean silentTitle) {
        setChangedFor(player, operation, ASetUtils.singleton(stage), silentTitle);
    }

    public void setChangedFor(Player player, AOperation operation, Set<String> stages) {
        setChangedFor(player, operation, stages, false);
    }

    public void setChangedFor(Player player, AOperation operation, Set<String> stages, boolean silentTitle) {
        AStagesUtils.checkPlayerStages(player, operation, stages);

        StageSyncedPlayerEvent event = new StageSyncedPlayerEvent(player, operation, stages);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            ANetworking.sendToPlayer((ServerPlayer) player, new ClientStagesSyncerS2CPacket(stages, operation));

            if (!silentTitle) {
                if (player instanceof ServerPlayer serverPlayer) {
                    stages.forEach(stage -> ATitleUtils.showTitles(serverPlayer, operation, stage));
                }
            }

            switch (operation) {
                case ADD -> MinecraftForge.EVENT_BUS.post(new StageAddedPlayerEvent(player, ASetUtils.getOnlyElement(stages)));
                case ADD_ALL -> MinecraftForge.EVENT_BUS.post(new AllStagesAddedPlayerEvent(player, stages));
                case REMOVE -> MinecraftForge.EVENT_BUS.post(new StageRemovedPlayerEvent(player, ASetUtils.getOnlyElement(stages)));
                case REMOVE_ALL -> MinecraftForge.EVENT_BUS.post(new AllStagesRemovedPlayerEvent(player, stages));
                case LOGIN -> MinecraftForge.EVENT_BUS.post(new StageLoginPlayerEvent(player, stages));
            }
        } else {
            switch (event.getOperation()) {
                case ADD -> this.stages.remove(ASetUtils.getOnlyElement(stages));
                case ADD_ALL, LOGIN -> this.stages.removeAll(stages);
                case REMOVE -> this.stages.add(ASetUtils.getOnlyElement(stages));
                case REMOVE_ALL -> this.stages.addAll(stages);
            }
        }
    }

    public List<String> getStages() {
        if (stages == null) {
            return Collections.emptyList();
        }

        return stages;
    }

    public void addStage(String stage) {
        if (stages.contains(stage)) { return; }
        AStagesUtils.checkPlayerStages(null, AOperation.ADD, ASetUtils.singleton(stage));

        stages.add(stage);
    }

    public void removeAllStages() {
        stages = new ArrayList<>();
    }

    public AStatus removeStage(String stage) {
        return stages.remove(stage) ? AStatus.SUCCESS : AStatus.NOT_PRESENT;
    }

    public void copyFrom(PlayerStage source) {
        stages = source.stages;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (stages == null) { return; }
        if (stages.isEmpty()) { return; }

        nbt.putInt("stage_size", stages.size());

        for (int i = 0; i < stages.size(); i++) {
            nbt.putString("stage_" + i, stages.get(i));
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        var size = nbt.getInt("stage_size");

        if (size > 0) {
            stages = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                stages.add(nbt.getString("stage_" + i));
            }
        }
    }
}
