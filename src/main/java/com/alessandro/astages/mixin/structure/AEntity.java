package com.alessandro.astages.mixin.structure;

import com.alessandro.astages.api.develop.Info;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Entity.class)
@Info("Pay attention about lithium interactions!")
public abstract class AEntity {
//    @Inject(method = "collideBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getWorldBorder()Lnet/minecraft/world/level/border/WorldBorder;"))
//    private static void astages$collideBoundingBox(Entity pEntity, Vec3 pVec, AABB pCollisionBox, Level pLevel, List<VoxelShape> pPotentialHits, CallbackInfoReturnable<Vec3> cir, @Local LocalRef<ImmutableList.Builder<VoxelShape>> builder) {
//        if (pEntity instanceof ServerPlayer player && pLevel instanceof ServerLevel serverLevel) {
//            AStages.LOGGER.debug("Injected!");
//            var newShapes = AStructureUtils.isCloseToStructure(player, serverLevel.structureManager(), serverLevel);
//            builder.set(builder.get().addAll(newShapes));
//        }
//    }


//    @Inject(method = "collideBoundingBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getWorldBorder()Lnet/minecraft/world/level/border/WorldBorder;"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private static void astages$collideBoundingBox(Entity entity, Vec3 vec, AABB collisionBox, Level level, List<VoxelShape> potentialHits, CallbackInfoReturnable<Vec3> cir, ImmutableList.Builder<VoxelShape> builder) {
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
//    }
}
