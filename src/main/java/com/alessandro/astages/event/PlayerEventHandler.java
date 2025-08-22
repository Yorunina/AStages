package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.ContainerChangedEvent;
import com.alessandro.astages.event.custom.PlayerInventoryChangedEvent;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.SyncOperation;
import com.alessandro.astages.api.annotation.nullability.NotNullParams;
import com.alessandro.astages.api.annotation.develop.Info;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStages.MODID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            event.getEntity().getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> playerStage.setChangedFor(event.getEntity(), PlayerStage.Operation.GET, null));
        }

        event.getEntity().inventoryMenu.addSlotListener(new AInventorySlotListener(event.getEntity()));

        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
            ARestrictionManager.clearClientOnLogin(player);
            ARestrictionManager.reflectServerStagesChangesToClients(player, player.server);
            ARestrictionManager.reflectSimpleIdsChangesToClients(player, ARestrictionManager.SIMPLE_IDS, SyncOperation.ADD);
            ARestrictionManager.reflectAllStagesChangesToClients(player, ARestrictionManager.ALL_STAGES, SyncOperation.ADD);
            ARestrictionManager.clientSynchronization(player);
        }
    }

    @SubscribeEvent
    public static void onAttachedCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerStageProvider.PLAYER_STAGE).isPresent()) {
                event.addCapability(AStagesUtil.fromNamespaceAndPath("properties"), new PlayerStageProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();

        event.getOriginal().getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(oldStore -> {
            event.getEntity().getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
            });
        });

        event.getOriginal().invalidateCaps();

        event.getEntity().inventoryMenu.addSlotListener(new AInventorySlotListener(event.getEntity()));
    }

    @Info("For container checking!")
    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        event.getContainer().addSlotListener(new AContainerSlotListener(event.getEntity()));

        CommonEventSettings.setPlayerAnotherContainerOpened(event.getEntity(), event.getContainer());
        CommonEventSettings.containerChanged();
    }

    @Info("For inventory checking!")
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
