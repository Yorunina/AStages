package com.alessandro.astages.api.holder;

import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.core.AClientRestrictionManager;

@NotNullMethodsReturn
public class AClientHolder {
    private final boolean isServer;
    private final boolean isPlayer;

    private AClientHolder(boolean isServer, boolean isPlayer) {
        this.isServer = isServer;
        this.isPlayer = isPlayer;
    }

    public static AClientHolder player() {
        return new AClientHolder(false, true);
    }

    public static AClientHolder server() {
        return new AClientHolder(true, false);
    }

    public static AClientHolder serverAndPlayer() {
        return new AClientHolder(true, true);
    }

    public boolean isServerActive() {
        return isServer;
    }

    public boolean isPlayerActive() {
        return isPlayer;
    }

    public AStageHolder stages() {
        if (isServer && isPlayer) { // Server stages is prioritized!
            return AStageHolder.init()
                .hold(AStageType.PLAYER, AStagesClientUtils.getStages())
                .hold(AStageType.SERVER, AClientRestrictionManager.SERVER_STAGES);
        }

        if (isPlayer) {
            return AStageHolder.initAndHold(AStageType.PLAYER, AStagesClientUtils.getStages());
        }

        if (isServer) {
            return AStageHolder.initAndHold(AStageType.SERVER, AClientRestrictionManager.SERVER_STAGES);
        }

        return AStageHolder.init();
    }
}
