package com.alessandro.astages.event;

import com.alessandro.astages.Astages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@EventBusSubscriber(modid = Astages.MODID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void playerTick(@NotNull PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) {
            Astages.LOGGER.debug("Client: {}", ClientPlayerStage.getPlayerStages());
//            return;
        } else {
            if (event.getEntity().getData(PlayerStageProvider.PLAYER_STAGE) != null) {
                Astages.LOGGER.debug("Server: {}", Objects.requireNonNull(event.getEntity().getData(PlayerStageProvider.PLAYER_STAGE)).getStages());
            }
        }
//        var cap = event.getEntity().getData(PlayerStageProvider.PLAYER_STAGE);

//        Astages.LOGGER.debug("STAGES: {}", cap.getStages());
    }

    @SubscribeEvent
    public static void playerLoggedIn(@NotNull PlayerEvent.PlayerLoggedInEvent event) {
        var playerStage = event.getEntity().getData(PlayerStageProvider.PLAYER_STAGE);
        playerStage.setChangedFor((ServerPlayer) event.getEntity(), PlayerStage.Operation.LOGIN, null);
    }
}
