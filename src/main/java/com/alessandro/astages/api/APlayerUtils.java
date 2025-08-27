package com.alessandro.astages.api;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

@NotNullParamsAndMethodsReturn
public class APlayerUtils {
    public static @Nullable Player getPlayerFromUUID(MinecraftServer server, UUID uuid) {
        return server.getPlayerList().getPlayer(uuid);
    }

    public static @Nullable Player getNearestPlayer(Level level, BlockPos pos) {
        return getNearestPlayer(level, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static @Nullable Player getNearestPlayer(Level level, Vec3 pos) {
        var players = level.players();
        var minDistance = Double.MAX_VALUE;
        Player toReturn = null;

        for (Player player : players) {
            var distance = player.distanceToSqr(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
            if (distance < minDistance) {
                minDistance = distance;
                toReturn = player;
            }
        }

        return toReturn;
    }
}
