package com.alessandro.astages.capability;

import com.alessandro.astages.core.stage.AStageManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Contract;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
// @OnlyIn(Dist.DEDICATED_SERVER)
public class ServerStageData extends SavedData {
    private static final String STAGE_ID = "astages_server_stages";
    // private static final String STAGE_LIST_ID = "astages_server_stages_list";
    private List<String> serverStages = new ArrayList<>();

    public void add(String... stages) {
        var list = List.of(stages);
        list.forEach(ServerStageData::checkStage);

        serverStages.addAll(list);
        setDirty();
    }

    public void set(List<String> stages) {
        serverStages = stages;
        setDirty();
    }

    public void remove(String... stages) {
        serverStages.removeAll(List.of(stages));
        setDirty();
    }

    public void removeAll() {
        serverStages.clear();
        setDirty();
    }

    public static void checkStage(String stage) {
        if (AStageManager.isPlayerOnly(stage)) {
            throw new IllegalArgumentException("Trying to add stage " + stage + " that is marked as available in player only!");
        }
    }

    public List<String> get() {
        return serverStages;
    }

    public boolean has(String stage) {
        return serverStages.contains(stage);
    }

    @Contract(" -> new")
    public static ServerStageData create() {
        return new ServerStageData();
    }

    public static ServerStageData load(CompoundTag nbt) {
//        var tagList = tag.getList(STAGE_LIST_ID, Tag.TAG_STRING);
//        var newData = create();
//
//        for (int i = 0; i < tagList.size(); i++) {
//            newData.add(tagList.getString(i));
//        }

        var newData = create();
        var size = nbt.getInt("server_stage_size");

        for (int i = 0; i < size; i++) {
            newData.serverStages.add(nbt.getString("server_stage_" + i));
        }

        return newData;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        if (serverStages.isEmpty()) { return nbt; }
//        var tagList = new ListTag();
//
//        for (String serverStage : serverStages) {
//            var tag = new CompoundTag();
//            tag.putString("value", serverStage);
//            tagList.add(tag);
//        }
//
//        nbt.put(STAGE_LIST_ID, tagList);

        nbt.putInt("server_stage_size", serverStages.size());

        for (int i = 0; i < serverStages.size(); i++) {
            nbt.putString("server_stage_" + i, serverStages.get(i));
        }

        return nbt;
    }

    public static ServerStageData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(ServerStageData::load, ServerStageData::create, STAGE_ID);
    }

    public static ServerStageData getData(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(ServerStageData::load, ServerStageData::create, STAGE_ID);
    }
}
