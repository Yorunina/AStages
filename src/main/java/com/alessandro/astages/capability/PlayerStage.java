package com.alessandro.astages.capability;

import com.alessandro.astages.Astages;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerStage implements INBTSerializable<CompoundTag> {
    public enum Status {
        SUCCESS, NOT_PRESENT
    }

    public enum Operation {
        ADD, REMOVE, REMOVE_ALL, GET, LOGIN
    }

    private List<String> stages = new ArrayList<>();

    public void setChangedFor(ServerPlayer player, @NotNull Operation operation, String stage) {
        PacketDistributor.sendToPlayer(player, new StageDataSyncS2CPacket(stages));
        Astages.LOGGER.debug("SERVER UPDATED!");

//        switch (operation) {
//            case ADD -> stages.add(stage);
//            case REMOVE -> stages.remove(stage);
//            case REMOVE_ALL, GET, LOGIN -> Astages.LOGGER.debug("CANCELLING NOT YET IMPLEMENTED!");
//        }

//        StageSyncedPlayerEvent event = new StageSyncedPlayerEvent(player, operation, stage);
//        MinecraftForge.EVENT_BUS.post(event);
//
//        if (!event.isCanceled()) {
//            ModNetworking.sendToPlayer(new StageDataSyncS2CPacket(stages), (ServerPlayer) player);
//
//            switch (operation) {
//                case ADD -> MinecraftForge.EVENT_BUS.post(new StageAddedPlayerEvent(player, stage));
//                case REMOVE -> MinecraftForge.EVENT_BUS.post(new StageRemovedPlayerEvent(player, stage));
//                case REMOVE_ALL -> MinecraftForge.EVENT_BUS.post(new AllStageRemovedPlayerEvent(player, stages));
//                case GET -> MinecraftForge.EVENT_BUS.post(new StageGetPlayerEvent(player, stages));
//                case LOGIN -> AStages.LOGGER.debug("NOT YET IMPLEMENTED!");
//            }
//        } else {
//            switch (event.getOperation()) {
//                case ADD -> stages.remove(stage);
//                case REMOVE -> stages.add(stage);
//                case REMOVE_ALL, GET, LOGIN -> AStages.LOGGER.debug("CANCELLING NOT YET IMPLEMENTED!");
//            }
//        }
    }
//
//    public List<String> setStages(List<String> stages) {
//        this.stages = stages;
//
//        return stages;
//    }

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

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        return saveNBTData();
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag tag) {
        loadNBTData(tag);
    }

    public CompoundTag saveNBTData() {
        var nbt = new CompoundTag();
        if (stages == null) { return nbt; }
        if (stages.isEmpty()) { return nbt; }

        nbt.putInt("stage_size", stages.size());

        for (int i = 0; i < stages.size(); i++) {
            nbt.putString("stage_" + i, stages.get(i));
        }

        return nbt;
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
