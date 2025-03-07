package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.AProvider;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.PlayerInventoryChangedEvent;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = AStages.MODID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.@NotNull PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            var playerStage = event.getEntity().getData(AProvider.PLAYER_STAGE);
            playerStage.setChangedFor(event.getEntity(), PlayerStage.Operation.GET, null);
        }

        event.getEntity().inventoryMenu.addSlotListener(new AInventorySlotListener(event.getEntity()));

        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
            ARestrictionManager.clientSynchronization(player);
            ARestrictionManager.reflectServerStagesChangesToClients(player, player.server);
        }
    }

//    @SubscribeEvent
//    public static void onAttachedCapabilities(@NotNull AttachCapabilitiesEvent<Entity> event) {
//        if (event.getObject() instanceof Player) {
//            if (!event.getObject().getCapability(PlayerStageProvider.PLAYER_STAGE).isPresent()) {
//                event.addCapability(new ResourceLocation(AStages.MODID, "properties"), new PlayerStageProvider());
//            }
//        }
//    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.@NotNull Clone event) {
//        event.getOriginal().revive();

        var oldStore = event.getOriginal().getData(AProvider.PLAYER_STAGE);
        var newStore = event.getEntity().getData(AProvider.PLAYER_STAGE);
        newStore.copyFrom(oldStore);

//        event.getOriginal().invalidateCaps();

        event.getEntity().inventoryMenu.addSlotListener(new AInventorySlotListener(event.getEntity()));
    }

    @Info("For inventory checking!")
    @SubscribeEvent
    public static void onContainerOpen(@NotNull PlayerContainerEvent event) {
        event.getContainer().addSlotListener(new AInventorySlotListener(event.getEntity()));
    }

    @Info("For inventory checking!")
    @SubscribeEvent
    public static void onInventoryChanged(@NotNull PlayerInventoryChangedEvent event) {
        CommonEventSettings.isInventoryChanged = true;
        CommonEventSettings.slotChanged = event.getSlot();
    }

//    @Info("For armor checking!")
//    @SubscribeEvent
//    public static void livingEquipmentChanged(@NotNull LivingEquipmentChangeEvent event) {
//        if (event.getEntity() instanceof Player) {
//            ServerEventHandler.isInventoryChanged = true;
//            CommonEventSettings.isInventoryChanged = true;
//            CommonEventSettings.slotChanged = event.getSlot().getIndex();
//        }
//    }

//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
//        if (event.player.getServer() != null) {
//            AStages.LOGGER.debug(ServerStageData.getData(event.player.getServer()).get().toString());
//        }
//    }

    @Info("For whole inventory checking!")
    @SubscribeEvent
    public static void onStageSynced(StageSyncedPlayerEvent event) {
        CommonEventSettings.isInventoryChanged = true;
        CommonEventSettings.slotChanged = null;
    }
}
