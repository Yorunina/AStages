package com.alessandro.astages.api;

import com.alessandro.astages.api.develop.NotYetImplemented;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.capability.ServerStageData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    private void addPlayer(Player player) {
        players.add(player);
    }

    @NotYetImplemented("Replace with StageHolder!")
    public List<String> stages() {
        if (isPlayer && !isMultiple) {
            return AStagesUtils.getStages(players.get(0));
        }

        if (isServer) {
            return ServerStageData.getData(ServerLifecycleHooks.getCurrentServer()).get();
        }

        if (isPlayer) {
            var stageSet = new HashSet<String>();
            players.forEach(player -> stageSet.addAll(AStagesUtils.getStages(player)));
            return new ArrayList<>(stageSet);
        }

        return new ArrayList<>();
    }
}
