package com.alessandro.astages.infrastructure.networking.packet.structure;

import com.alessandro.astages.api.misc.Twin;
import com.alessandro.astages.api.network.AByteBuf;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.collision.StructureCollisionManager;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

@NotNullParams
public class SyncRestrictedStructuresS2C implements AStagesPacket {
    private final ResourceKey<Level> dimension;
    private final ChunkPos chunkPos;
    private final List<Twin<String, BoundingBox>> boxes;

    public SyncRestrictedStructuresS2C(ResourceKey<Level> dimension, ChunkPos chunkPos, List<Twin<String, BoundingBox>> boxes) {
        this.dimension = dimension;
        this.chunkPos = chunkPos;
        this.boxes = boxes;
    }

    public SyncRestrictedStructuresS2C(FriendlyByteBuf buf) {
        dimension = buf.readResourceKey(Registries.DIMENSION);
        chunkPos = buf.readChunkPos();
        boxes = buf.readList(b -> Twin.decode(buf, FriendlyByteBuf::readUtf, AByteBuf::readBoundingBox));
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceKey(dimension);
        buf.writeChunkPos(chunkPos);
        buf.writeCollection(boxes, (b, value) -> Twin.encode(b, value, FriendlyByteBuf::writeUtf, AByteBuf::writeBoundingBox));
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        StructureCollisionManager.CLIENT_INSTANCE.populateClientCacheForChunk(dimension, chunkPos, boxes);
    }
}