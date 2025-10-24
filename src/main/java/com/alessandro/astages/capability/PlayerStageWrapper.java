package com.alessandro.astages.capability;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@NotNullParamsAndMethodsReturn
@SuppressWarnings("removal")
public class PlayerStageWrapper {
    public static LazyOptional<PlayerStage> getPlayerCapability(Player player) {
        return player.getCapability(PlayerStageProvider.PLAYER_STAGE);
    }

    public static List<String> getStages(Player player) {
        AtomicReference<List<String>> playerStages = new AtomicReference<>(new ArrayList<>());
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> playerStages.set(playerStage.getStages()));
        return playerStages.get();
    }

    public static void onAttachedCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerStageProvider.PLAYER_STAGE).isPresent()) {
                event.addCapability(AResourceLocation.fromNamespaceAndPath("properties"), new PlayerStageProvider());
            }
        }
    }
}
