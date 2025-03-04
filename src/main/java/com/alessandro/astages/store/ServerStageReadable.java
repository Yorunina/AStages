package com.alessandro.astages.store;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface ServerStageReadable<R extends ARestriction<R, ?, V>, V> {
    R getRestriction(MinecraftServer server, V object);

    R getRestriction(V object, @Nullable Player player, @Nullable MinecraftServer sever);
}
