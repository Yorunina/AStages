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

        var listTag = (ListTag) nbt.get("structure_blocks");
        if (listTag != null) {
            listTag.forEach(tag -> {
                if (tag instanceof LongTag longTag) {
                    newData.placedByPlayerBlocks.add(BlockPos.of(longTag.getAsLong()));
                }
            });
        }

        return newData;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        var listTag = new ListTag();
        placedByPlayerBlocks.forEach(blockPos -> listTag.add(LongTag.valueOf(blockPos.asLong())));
        nbt.put("structure_blocks", listTag);

        return nbt;
    }

    public static StructureData getData(ServerLevel level, String structureId) {
        return level.getDataStorage().computeIfAbsent(StructureData::load, StructureData::create, STRUCTURE_ID + structureId);
    }

    public static StructureData getData(MinecraftServer server, String structureId) {
        return server.overworld().getDataStorage().computeIfAbsent(StructureData::load, StructureData::create, STRUCTURE_ID + structureId);
    }
}
