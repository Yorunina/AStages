package com.alessandro.astages.mixin.structure;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(Entity.class)
public abstract class AEntity {
    @Inject(method = "collideBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getWorldBorder()Lnet/minecraft/world/level/border/WorldBorder;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void astages$collideBoundingBox(Entity entity, Vec3 vec, AABB collisionBox, Level level, List<VoxelShape> potentialHits, CallbackInfoReturnable<Vec3> cir, ImmutableList.Builder<VoxelShape> builder) {
//        if (entity instanceof ServerPlayer player && level instanceof ServerLevel serverLevel) {
//            // var newPos = player.getOnPos().mutable().move(0, 2, 0).move(1, 0, 0);
//            var structure = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE).get(new ResourceLocation("minecraft:pillager_outpost"));
//            var s = serverLevel.structureManager().getStructureWithPieceAt(player.blockPosition(), structure);
//
//            if (s.isValid()) {
//                var bb = s.getBoundingBox();
//                var newShape = Shapes.box(bb.minX(), bb.minY(), bb.minZ(), bb.maxX(), bb.maxY(), bb.maxZ());
//                builder.add(newShape);
//                ARenderer.addBoundingBox(bb);
//            }
//        }

//        if (entity instanceof Player) { }
//        builder.add(Shapes.box(-2315, 70, -2205, -2311, 71, -2200));
    }
}
