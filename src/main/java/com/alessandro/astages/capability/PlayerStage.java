package com.alessandro.astages.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.event.custom.actions.AllStageRemovedPlayerEvent;
import com.alessandro.astages.event.custom.actions.StageAddedPlayerEvent;
import com.alessandro.astages.event.custom.actions.StageGetPlayerEvent;
import com.alessandro.astages.event.custom.actions.StageRemovedPlayerEvent;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoRegisterCapability
public class PlayerStage {
    public enum Status {
        SUCCESS, NOT_PRESENT
    }

    public enum Operation {
        ADD, REMOVE, REMOVE_ALL, GET, LOGIN
    }

    private List<String> stages = new ArrayList<>();

    @Info("Not required, for commands only!")
    public void setChangedFor(Player player, @NotNull Operation operation, String stage) {
        setChangedFor(player, operation, stage, false);
    }

    @Info("TO BE TESTED!")
    public void setChangedFor(Player player, @NotNull Operation operation, @Nullable String stage, boolean silentTitle) {
        StageSyncedPlayerEvent event = new StageSyncedPlayerEvent(player, operation, stage);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            ModNetworking.sendToPlayer(new StageDataSyncS2CPacket(stages), (ServerPlayer) player);

            if (!silentTitle && stage != null) {
                if (player instanceof ServerPlayer serverPlayer) {
                    AStagesUtil.showTitles(serverPlayer, operation, stage);
                }
            }

            switch (operation) {
                case ADD -> MinecraftForge.EVENT_BUS.post(new StageAddedPlayerEvent(player, stage));
                case REMOVE -> MinecraftForge.EVENT_BUS.post(new StageRemovedPlayerEvent(player, stage));
                case REMOVE_ALL -> MinecraftForge.EVENT_BUS.post(new AllStageRemovedPlayerEvent(player, stages));
                case GET -> MinecraftForge.EVENT_BUS.post(new StageGetPlayerEvent(player, stages));
                case LOGIN -> AStages.LOGGER.debug("NOT YET IMPLEMENTED!");
            }
        } else {
            switch (event.getOperation()) {
                case ADD -> stages.remove(stage);
                case REMOVE -> stages.add(stage);
                case REMOVE_ALL, GET, LOGIN -> AStages.LOGGER.debug("CANCELLING NOT YET IMPLEMENTED!");
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

        stages.add(stage);
    }

    public void removeAllStages() {
        stages = new ArrayList<>();
    }

    public Status removeStage(String stage) {
        return stages.remove(stage) ? Status.SUCCESS : Status.NOT_PRESENT;
    }

    public void copyFrom(@NotNull PlayerStage source) {
        stages = source.stages;
    }

    public void saveNBTData(@NotNull CompoundTag nbt) {
        if (stages == null) { return; }
        if (stages.isEmpty()) { return; }

        nbt.putInt("stage_size", stages.size());

        for (int i = 0; i < stages.size(); i++) {
            nbt.putString("stage_" + i, stages.get(i));
        }
    }

    public void loadNBTData(@NotNull CompoundTag nbt) {
        var size = nbt.getInt("stage_size");

        if (size > 0) {
            stages = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                stages.add(nbt.getString("stage_" + i));
            }
        }
    }
}
