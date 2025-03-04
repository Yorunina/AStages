package com.alessandro.astages.capability;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StructureData extends SavedData {
    private static final String STRUCTURE_ID = "astages_structure_data_";
    // private String structureId;
    // private HashMap<BlockPos, List<BlockPos>> placedByPlayerBlocks; // StructureStart: Block Positions
    private final HashSet<BlockPos> placedByPlayerBlocks = new HashSet<>(); // StructureStart: Block Positions

    public static StructureData create() {
        return new StructureData();
    }

    public boolean isBlockPlacedByPlayer(BlockPos blockPos) {
        return placedByPlayerBlocks.contains(blockPos);
    }

    public void add(BlockPos... pos) {
        placedByPlayerBlocks.addAll(List.of(pos));
        setDirty();
    }

    public void remove(BlockPos... pos) {
        List.of(pos).forEach(placedByPlayerBlocks::remove);
        setDirty();
    }

    public static StructureData load(CompoundTag nbt) {
        var newData = create();

        // newData.setStructureId(nbt.getString("structure_id"));

        var listTag = (ListTag) nbt.get("structure_blocks");
        if (listTag != null) {
            listTag.forEach(tag -> {
                if (tag instanceof LongTag longTag) {
                    newData.placedByPlayerBlocks.add(BlockPos.of(longTag.getAsLong()));
                }
            });
        }

//        var structureSize = nbt.getInt("structure_size");
//
//        for (int i = 0; i < structureSize; i++) {
//            var structureStartId = "structure_start_" + i;
//            var structureStart = BlockPos.of(nbt.getLong(structureStartId));
//
//            var structureListId = "structure_list_" + i;
//            var listTag = (ListTag) nbt.get(structureListId);
//            var blockList = new ArrayList<BlockPos>();
//            Objects.requireNonNull(listTag).forEach(tag -> {
//                if (tag instanceof LongTag longTag) {
//                    blockList.add(BlockPos.of(longTag.getAsLong()));
//                }
//            });
//
//            newData.placedByPlayerBlocks.clear();
//            newData.placedByPlayerBlocks.put(structureStart, blockList);
//        }

        return newData;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        // nbt.putString("structure_id", structureId);

        var listTag = new ListTag();
        placedByPlayerBlocks.forEach(blockPos -> {
            listTag.add(LongTag.valueOf(blockPos.asLong()));
        });

        nbt.put("structure_blocks", listTag);

//
//        var allStructures = placedByPlayerBlocks.keySet().stream().toList();
//        nbt.putInt("structure_size", allStructures.size());
//
//        for (int i = 0; i < allStructures.size(); i++) {
//            var structureStart = allStructures.get(i);
//            var structureStartId = "structure_start_" + i;
//            nbt.putLong(structureStartId, structureStart.asLong());
//
//            var blockList = placedByPlayerBlocks.get(structureStart);
//            var listTag = new ListTag();
//            blockList.forEach(blockPos -> {
//                listTag.add(LongTag.valueOf(blockPos.asLong()));
//            });
//
//            nbt.put("structure_list_" + i, listTag);
//        }

        return nbt;
    }

    public static StructureData getData(ServerLevel level, String structureId) {
        return level.getDataStorage().computeIfAbsent(StructureData::load, StructureData::create, STRUCTURE_ID + structureId);
    }

    public static StructureData getData(MinecraftServer server, String structureId) {
        return server.overworld().getDataStorage().computeIfAbsent(StructureData::load, StructureData::create, STRUCTURE_ID + structureId);
    }

//    public String getStructureId() {
//        return structureId;
//    }
//
//    public void setStructureId(String structureId) {
//        this.structureId = structureId;
//    }
}
