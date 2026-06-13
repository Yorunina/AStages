package com.alessandro.astages.infrastructure.hook.stage;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.AFileIOUtils;
import com.alessandro.astages.infrastructure.capability.OfflinePlayerStage;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class PlayerStagesEvents {
    @SubscribeEvent
    public static void serverStarted(ServerStartingEvent event) {
        OfflinePlayerStage.setUUIDToUsernameMap(
            AFileIOUtils.readMapOrDefault(
                OfflinePlayerStage.getConfigFile(OfflinePlayerStage.UUID_TO_USERNAME_FILE),
                UUID.class,
                String.class
            )
        );

        OfflinePlayerStage.setUsernameToUUIDMap(
            AFileIOUtils.readMapOrDefault(
                OfflinePlayerStage.getConfigFile(OfflinePlayerStage.USERNAME_TO_UUID_FILE),
                String.class,
                UUID.class
            )
        );
    }

    @SubscribeEvent
    public static void serverStopped(ServerStoppingEvent event) {
        AFileIOUtils.writeFileContent(OfflinePlayerStage.getConfigFile(OfflinePlayerStage.UUID_TO_USERNAME_FILE), OfflinePlayerStage.getUUIDToUsernameMap());
        AFileIOUtils.writeFileContent(OfflinePlayerStage.getConfigFile(OfflinePlayerStage.USERNAME_TO_UUID_FILE), OfflinePlayerStage.getUsernameToUUIDMap());
    }

    @Info("Migration purpose only!")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var playerUUID = event.getEntity().getUUID();
        var playerName = event.getEntity().getGameProfile().getName();
        OfflinePlayerStage.setPlayerNameToUUIDAssociation(playerName, playerUUID);

        var file = OfflinePlayerStage.getPermanentStagesFile(event.getEntity());
        var stageList = AFileIOUtils.readList(file, String.class);

        if (stageList == null) {
            var oldList = OfflinePlayerStage.getPlayerStagesFromCapability(event.getEntity());
            AFileIOUtils.writeFileContent(file, oldList);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        var player = event.getEntity();
        OfflinePlayerStage.markAsDirty(player);
        OfflinePlayerStage.clearCache(player);
    }
}