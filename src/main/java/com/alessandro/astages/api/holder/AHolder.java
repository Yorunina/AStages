package com.alessandro.astages.api.holder;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.capability.OfflinePlayerStage;
import com.alessandro.astages.capability.ServerStageData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public class AHolder {
    private final boolean isServer;
    private final boolean isPlayer;
    private final boolean isMultiple;

    private final List<Player> players = new ArrayList<>();

    private AHolder(boolean isServer, boolean isPlayer, boolean isMultiple) {
        this.isServer = isServer;
        this.isPlayer = isPlayer;
        this.isMultiple = isMultiple;
    }

    public static AHolder player(Player player) {
        var toReturn = new AHolder(false, true, false);
        toReturn.addPlayer(player);
        return toReturn;
    }

    public static AHolder players(List<Player> players) {
        var toReturn = new AHolder(false, true, true);
        players.forEach(toReturn::addPlayer);
        return toReturn;
    }

    public static AHolder server() {
        return new AHolder(true, false, false);
    }

    public static AHolder serverAndPlayer(Player player) {
        return new AHolder(true, true, false).addPlayer(player);
    }

    private AHolder addPlayer(Player player) {
        players.add(player);
        return this;
    }

    public boolean isServerActive() {
        return isServer;
    }

    public boolean isPlayerActive() {
        return isPlayer;
    }

    public AStageHolder getStages() {
        if (isPlayer && !isMultiple) {
            return AStageHolder.initAndHold(AStageType.PLAYER, OfflinePlayerStage.getPlayerStagesFromFile(players.get(0)));
        }

        if (isServer && isPlayer) { // Server stages is prioritized!
            return AStageHolder.init()
                .hold(AStageType.PLAYER, OfflinePlayerStage.getPlayerStagesFromFile(players.get(0)))
                .hold(AStageType.SERVER, ServerStageData.getData(ServerLifecycleHooks.getCurrentServer()).getServerStages());
        }

        if (isServer) {
            return AStageHolder.initAndHold(AStageType.SERVER, ServerStageData.getData(ServerLifecycleHooks.getCurrentServer()).getServerStages());
        }

        if (isPlayer) {
            var stageSet = new HashSet<String>();
            players.forEach(player -> stageSet.addAll(OfflinePlayerStage.getPlayerStagesFromFile(players.get(0))));
            return AStageHolder.initAndHold(AStageType.PLAYER, stageSet);
        }

        return AStageHolder.init();
    }

    public void perform(Consumer<Player> forPlayer, Consumer<MinecraftServer> forServer) {
        advancedPerform(forPlayer, players -> {
            for (var player : players) {
                forPlayer.accept(player);
            }
        }, forServer);
    }

    public void advancedPerform(Consumer<Player> forPlayer, Consumer<List<Player>> forPlayers, Consumer<MinecraftServer> forServer) {
        if (isServer) {
            forServer.accept(ServerLifecycleHooks.getCurrentServer());
        }

        if (isPlayer && !isMultiple) {
            forPlayer.accept(players.get(0));
        }

        if (isPlayer && isMultiple) {
            forPlayers.accept(players);
        }
    }
}
