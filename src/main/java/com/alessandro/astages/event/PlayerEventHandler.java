package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.AProvider;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.ContainerChangedEvent;
import com.alessandro.astages.event.custom.PlayerInventoryChangedEvent;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.util.SyncOperation;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = AStages.MODID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            var playerStage = event.getEntity().getData(AProvider.PLAYER_STAGE);
            playerStage.setChangedFor(event.getEntity(), PlayerStage.Operation.GET, null);
        }

        event.getEntity().inventoryMenu.addSlotListener(new AInventorySlotListener(event.getEntity()));

        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
            ARestrictionManager.clearClientOnLogin(player);
            ARestrictionManager.reflectServerStagesChangesToClients(player, player.server);
            ARestrictionManager.reflectSimpleIdsChangesToClients(player, new ArrayList<>(ARestrictionManager.SIMPLE_IDS), SyncOperation.ADD);
            ARestrictionManager.reflectAllStagesChangesToClients(player, new ArrayList<>(ARestrictionManager.ALL_STAGES), SyncOperation.ADD);
            ARestrictionManager.clientSynchronization(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        var oldStore = event.getOriginal().getData(AProvider.PLAYER_STAGE);
        var newStore = event.getEntity().getData(AProvider.PLAYER_STAGE);
        newStore.copyFrom(oldStore);

        event.getEntity().inventoryMenu.addSlotListener(new AInventorySlotListener(event.getEntity()));
    }

    @Info("For container checking!")
    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        event.getContainer().addSlotListener(new AContainerSlotListener(event.getEntity()));

        CommonEventSettings.setPlayerAnotherContainerOpened(event.getEntity(), event.getContainer());
        CommonEventSettings.containerChanged();
    }

    @Info("For container checking!")
    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        CommonEventSettings.setPlayerAnotherContainerOpened(event.getEntity(), null);

        CommonEventSettings.allInventoryChanged();
    }

    @Info("For inventory checking!")
    @SubscribeEvent
    public static void onInventoryChanged(PlayerInventoryChangedEvent event) {
        CommonEventSettings.slotChanged(event.getSlot());
    }

    @Info("For whole inventory checking!")
    @SubscribeEvent
    public static void onStageSynced(StageSyncedPlayerEvent event) {
        CommonEventSettings.allInventoryChanged();
    }

    @Info("For container checking!")
    @SubscribeEvent
    public static void onPlayerContainerChanged(ContainerChangedEvent event) {
        CommonEventSettings.containerChanged();
    }
}
