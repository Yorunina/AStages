package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.PlayerInventoryChangedEvent;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.util.develop.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.@NotNull PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            event.getEntity().getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> playerStage.setChangedFor(event.getEntity(), PlayerStage.Operation.GET, null));
        }

        event.getEntity().inventoryMenu.addSlotListener(new AInventorySlotListener(event.getEntity()));

        if (!event.getEntity().level().isClientSide) {
            ARestrictionManager.RECIPE_INSTANCE.synchronizeWithClient((ServerPlayer) event.getEntity());
            ARestrictionManager.ORE_INSTANCE.synchronizeWithClient((ServerPlayer) event.getEntity());
            ARestrictionManager.MOB_INSTANCE.synchronizeWithClient((ServerPlayer) event.getEntity());
            ARestrictionManager.synchronizeOreStages((ServerPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onAttachedCapabilities(@NotNull AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerStageProvider.PLAYER_STAGE).isPresent()) {
                event.addCapability(new ResourceLocation(AStages.MODID, "properties"), new PlayerStageProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.@NotNull Clone event) {
        event.getOriginal().reviveCaps();

        event.getOriginal().getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(oldStore -> {
            event.getEntity().getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
            });
        });

        event.getOriginal().invalidateCaps();

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

    @Info("For whole inventory checking!")
    @SubscribeEvent
    public static void onStageSynced(StageSyncedPlayerEvent event) {
        CommonEventSettings.isInventoryChanged = true;
        CommonEventSettings.slotChanged = null;
    }
}
