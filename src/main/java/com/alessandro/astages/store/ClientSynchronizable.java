package com.alessandro.astages.store;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface ClientSynchronizable {
    void synchronizeWithClient(@Nullable ServerPlayer player);
}
