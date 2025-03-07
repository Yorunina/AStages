package com.alessandro.astages.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.event.custom.actions.AllStageRemovedPlayerEvent;
import com.alessandro.astages.event.custom.actions.StageAddedPlayerEvent;
import com.alessandro.astages.event.custom.actions.StageGetPlayerEvent;
import com.alessandro.astages.event.custom.actions.StageRemovedPlayerEvent;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;

public class PlayerStage implements INBTSerializable<CompoundTag> {
    public enum Status {
        SUCCESS, NOT_PRESENT
    }

    public enum Operation {
        ADD(0), REMOVE(1), REMOVE_ALL(2), GET(3), LOGIN(4);

        private final int id;

        Operation(int id) {
            this.id = id;
        }

        public int id() {
            return id;
        }

        @Contract(pure = true)
        public static int getId(@NotNull Operation operation) {
            return operation.id();
        }

        public static final IntFunction<Operation> BY_ID = ByIdMap.continuous(
            Operation::getId,
            Operation.values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
        );
    }

    private List<String> stages = new ArrayList<>();

    public PlayerStage(IAttachmentHolder iAttachmentHolder) { }

    public PlayerStage(List<String> stages) {
        this.stages = stages;
    }

    @Info("Not required, for commands only!")
    public void setChangedFor(Player player, @NotNull Operation operation, String stage) {
        setChangedFor(player, operation, stage, false);
    }

    @Info("TO BE TESTED!")
    public void setChangedFor(Player player, @NotNull Operation operation, @Nullable String stage, boolean silentTitle) {
//    }
//
//    public void setChangedFor(Player player, @NotNull Operation operation, List<String> stage) {

        StageSyncedPlayerEvent event = new StageSyncedPlayerEvent(player, operation, stage);
        NeoForge.EVENT_BUS.post(event);

        if (!event.isCanceled()) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new StageDataSyncS2CPacket(stages, operation));

            if (!silentTitle && stage != null) {
                if (player instanceof ServerPlayer serverPlayer) {
                    AStages.LOGGER.debug("TEXT!");
                    // serverPlayer.connection.send(new ClientboundSetTitleTextPacket(Component.literal("TEXT!")));
                    // AStagesUtil.showTitles(operation, stage);
                    AStagesUtil.showTitles(serverPlayer, operation, stage);
                }
            }

            switch (operation) {
                case ADD -> NeoForge.EVENT_BUS.post(new StageAddedPlayerEvent(player, stage));
                case REMOVE -> NeoForge.EVENT_BUS.post(new StageRemovedPlayerEvent(player, stage));
                case REMOVE_ALL -> NeoForge.EVENT_BUS.post(new AllStageRemovedPlayerEvent(player, stages));
                case GET -> NeoForge.EVENT_BUS.post(new StageGetPlayerEvent(player, stages));
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
