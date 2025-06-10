package com.alessandro.astages.plugin;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface AStagesPlugin {
    default void reloadBeforeScripts() { }
    default void clientSynchronization(@Nullable ServerPlayer player) { }
    default void reloadAfterScripts() { }
    default void reloadAfterScripts(MinecraftServer server) { }
    default void clearClientOnLogin() { }
    default void registerManagers(ManagerContainer container) { }

    ResourceLocation id();
}
