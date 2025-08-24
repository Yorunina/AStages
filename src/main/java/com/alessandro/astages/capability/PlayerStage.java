package com.alessandro.astages.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.event.custom.actions.*;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NotNullParams
@AutoRegisterCapability
public class PlayerStage {
//    public enum Status {
//        SUCCESS, NOT_PRESENT
//    }
//
//    public enum Operation {
//        ADD(true, true),
//        ADD_ALL(true, false),
//        REMOVE(false, true),
//        REMOVE_ALL(false, false),
//        GET(false, false),
//        LOGIN(false, false);
//
//        private final boolean needToBeChecked;
//        private final boolean supportOnlyOneStage;
//
//        Operation(boolean needToBeChecked, boolean supportOnlyOneStage) {
//            this.needToBeChecked = needToBeChecked;
//            this.supportOnlyOneStage = supportOnlyOneStage;
//        }
//
//        public boolean needToBeChecked() {
//            return needToBeChecked;
//        }
//
//        public boolean supportOnlyOneStage() {
//            return supportOnlyOneStage;
//        }
//    }

    private List<String> stages = new ArrayList<>();

//    public static void checkStage(String stage, AOperation operation) {
//        checkStages(Collections.singletonList(stage), operation);
//    }
//
//    public static void checkStages(List<String> stages, AOperation operation) {
//        if (operation.supportOnlyOneStage() && stages.size() != 1) {
//            throw new IllegalArgumentException("Trying to perform an action that supports single stage using multiple ones!");
//        }
//
//        if (!operation.needToBeChecked()) { return; }
//
//        for (var stage : stages) {
//            if (AStageManager.isServerOnly(stage)) {
//                throw new IllegalArgumentException("Trying to add stage " + stage + " that is marked as available in server only!");
//            }
//        }
//    }

    @Info("Not required, for commands only!")
    public void setChangedFor(Player player, AOperation operation, String stage) {
        setChangedFor(player, operation, stage, false);
    }

    public void setChangedFor(Player player, AOperation operation, String stage, boolean silentTitle) {
        setChangedFor(player, operation, Collections.singletonList(stage), silentTitle);
    }

    public void setChangedFor(Player player, AOperation operation, List<String> stages) {
        setChangedFor(player, operation, stages, false);
    }

    public void setChangedFor(Player player, AOperation operation, List<String> stages, boolean silentTitle) {
        AStagesUtils.checkStages(player, operation, stages);

        StageSyncedPlayerEvent event = new StageSyncedPlayerEvent(player, operation, stages);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            ANetworking.sendToPlayer(new StageDataSyncS2CPacket(stages, operation), (ServerPlayer) player);

            if (!silentTitle) {
                if (player instanceof ServerPlayer serverPlayer) {
                    stages.forEach(stage -> AStagesUtil.showTitles(serverPlayer, operation, stage));
                }
            }

            switch (operation) {
                case ADD -> MinecraftForge.EVENT_BUS.post(new StageAddedPlayerEvent(player, stages.get(0)));
                case ADD_ALL -> MinecraftForge.EVENT_BUS.post(new AllStageAddedPlayerEvent(player, stages));
                case REMOVE -> MinecraftForge.EVENT_BUS.post(new StageRemovedPlayerEvent(player, stages.get(0)));
                case REMOVE_ALL -> MinecraftForge.EVENT_BUS.post(new AllStageRemovedPlayerEvent(player, stages));
                case GET -> MinecraftForge.EVENT_BUS.post(new StageGetPlayerEvent(player, stages));
                case LOGIN -> MinecraftForge.EVENT_BUS.post(new StageLoginPlayerEvent(player, stages));
            }
        } else {
            switch (event.getOperation()) {
                case ADD -> this.stages.remove(stages.get(0));
                case ADD_ALL, LOGIN -> this.stages.removeAll(stages);
                case REMOVE -> this.stages.add(stages.get(0));
                case REMOVE_ALL -> this.stages.addAll(stages);
                case GET -> AStages.LOGGER.info("Get operation cannot be cancelled!");
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
        AStagesUtils.checkStage(null, AOperation.ADD, stage);

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
