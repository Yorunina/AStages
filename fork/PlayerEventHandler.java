package com.alessandro.astages.render;

import com.alessandro.astages.Astages;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Astages.MODID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void playerLoggedIn(@NotNull PlayerEvent.PlayerLoggedInEvent event) {
        var playerStage = event.getEntity().getData(PlayerStageProvider.PLAYER_STAGE);
        playerStage.setChangedFor((ServerPlayer) event.getEntity(), PlayerStage.Operation.GET, null);
    }
}
