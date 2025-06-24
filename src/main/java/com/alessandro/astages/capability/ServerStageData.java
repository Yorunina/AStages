package com.alessandro.astages.capability;

import com.alessandro.astages.core.stage.AStageManager;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.server.ServerStagesSyncerS2CPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Contract;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ServerStageData extends SavedData {
    private static final String STAGE_ID = "astages_server_stages";
    private List<String> serverStages = new ArrayList<>();

    public void add(String... stages) {
        var list = List.of(stages);
        list.forEach(ServerStageData::checkStage);

        serverStages.addAll(list);
        setDirty();
        synchronizeChanges();
    }

    public void set(List<String> stages) {
        serverStages = stages;
        setDirty();
        synchronizeChanges();
    }

    public void remove(String... stages) {
        serverStages.removeAll(List.of(stages));
        setDirty();
        synchronizeChanges();
    }

    public void removeAll() {
        serverStages.clear();
        setDirty();
        synchronizeChanges();
    }

    private void synchronizeChanges() {
        ModNetworking.sendTo(null, new ServerStagesSyncerS2CPacket(serverStages));
    }

    public static void checkStage(String stage) {
        if (AStageManager.isPlayerOnly(stage)) {
            throw new IllegalArgumentException("Trying to add stage " + stage + " that is marked as available in player only!");
        }
    }

    public List<String> get() {
        return serverStages;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean has(String stage) {
        return serverStages.contains(stage);
    }

    @Contract(" -> new")
    public static ServerStageData create() {
        return new ServerStageData();
    }

    public static ServerStageData load(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
        var newData = create();

        var listTag = (ListTag) nbt.get("server_stages");
        if (listTag != null) {
            listTag.forEach(tag -> {
                if (tag instanceof StringTag stringTag) {
                    newData.serverStages.add(stringTag.getAsString());
                }
            });
        }

        return newData;
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
        if (serverStages.isEmpty()) { return nbt; }

        var listTag = new ListTag();
        serverStages.forEach(stage -> listTag.add(StringTag.valueOf(stage)));
        nbt.put("server_stages", listTag);

        return nbt;
    }

    public static ServerStageData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(ServerStageData::create, ServerStageData::load), STAGE_ID);
    }

    public static ServerStageData getData(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(ServerStageData::create, ServerStageData::load), STAGE_ID);
    }
}
