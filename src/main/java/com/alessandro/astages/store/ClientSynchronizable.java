package com.alessandro.astages.store;

import com.alessandro.astages.util.annotations.Nullable;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.server.level.ServerPlayer;

@Info("Implement this interface to add client synchronization to a restriction!")
public interface ClientSynchronizable {
    void synchronizeWithClient(@Nullable ServerPlayer player);
}
