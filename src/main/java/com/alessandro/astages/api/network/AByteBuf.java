package com.alessandro.astages.api.network;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

@NotNullParamsAndMethodsReturn
public class AByteBuf {
    public static ChunkPos readChunkPos(FriendlyByteBuf buf) {
        return new ChunkPos(buf.readInt(), buf.readInt());
    }

    public static void writeChunkPos(FriendlyByteBuf buf, ChunkPos chunkPos) {
        buf.writeInt(chunkPos.x);
        buf.writeInt(chunkPos.z);
    }

    public static AABB readAABB(FriendlyByteBuf buf) {
        return new AABB(
            buf.readDouble(), buf.readDouble(), buf.readDouble(),
            buf.readDouble(), buf.readDouble(), buf.readDouble()
        );
    }

    public static void writeAABB(FriendlyByteBuf buf, AABB aabb) {
        buf.writeDouble(aabb.minX);
        buf.writeDouble(aabb.minY);
        buf.writeDouble(aabb.minZ);
        buf.writeDouble(aabb.maxX);
        buf.writeDouble(aabb.maxY);
        buf.writeDouble(aabb.maxZ);
    }

    public static BoundingBox readBoundingBox(FriendlyByteBuf buf) {
        return new BoundingBox(
            buf.readInt(), buf.readInt(), buf.readInt(),
            buf.readInt(), buf.readInt(), buf.readInt()
        );
    }

    public static void writeBoundingBox(FriendlyByteBuf buf, BoundingBox box) {
        buf.writeInt(box.minX());
        buf.writeInt(box.minY());
        buf.writeInt(box.minZ());
        buf.writeInt(box.maxX());
        buf.writeInt(box.maxY());
        buf.writeInt(box.maxZ());
    }
}
