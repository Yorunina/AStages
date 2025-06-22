package com.alessandro.astages.core;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.core.manager.*;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.dimension.DimensionIdsSyncerS2CPacket;
import com.alessandro.astages.networking.packet.ore.OreStagesSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestReloadS2CPacket;
import com.alessandro.astages.networking.packet.server.ServerStagesSyncerS2CPacket;
import com.alessandro.astages.store.ReloadType;
import com.alessandro.astages.util.ARestrictionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ARestrictionManager {
    // ADD SLOT RESTRICTION
    public static final AItemManager ITEM_INSTANCE = new AItemManager();
    public static final ADimensionManager DIMENSION_INSTANCE = new ADimensionManager();
    public static final AMobManager MOB_INSTANCE = new AMobManager();
    public static final AStructureManager STRUCTURE_INSTANCE = new AStructureManager();
    public static final ARecipeManager RECIPE_INSTANCE = new ARecipeManager();
    public static final AScreenManager SCREEN_INSTANCE = new AScreenManager();
    public static final AOreManager ORE_INSTANCE = new AOreManager();
    public static final APetManager PET_INSTANCE = new APetManager();
    public static final AEnchantManager ENCHANT_INSTANCE = new AEnchantManager();
    public static final ACropManager CROP_INSTANCE = new ACropManager();
    public static final AEffectManager EFFECT_INSTANCE = new AEffectManager();
    public static final ARegionManager REGION_INSTANCE = new ARegionManager();

    public static Set<String> ALL_STAGES = new HashSet<>();
    public static Set<String> ORE_STAGES = new HashSet<>();

    public static void reloadBeforeScripts() {
        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }

        ITEM_INSTANCE.reloadBeforeScripts();
        DIMENSION_INSTANCE.reloadBeforeScripts();
        MOB_INSTANCE.reloadBeforeScripts();
        STRUCTURE_INSTANCE.reloadBeforeScripts();
        RECIPE_INSTANCE.reloadBeforeScripts();
        SCREEN_INSTANCE.reloadBeforeScripts();
        ORE_INSTANCE.reloadBeforeScripts();
        PET_INSTANCE.reloadBeforeScripts();
        ENCHANT_INSTANCE.reloadBeforeScripts();
        CROP_INSTANCE.reloadBeforeScripts();
        EFFECT_INSTANCE.reloadBeforeScripts();

        ALL_STAGES.clear();
        ORE_STAGES.clear();

        PacketDistributor.sendToAllPlayers(new RequestReloadS2CPacket(ReloadType.CLIENT_BEFORE));
    }

    public static void clientSynchronization(@Nullable ServerPlayer player) {
        AStages.TIMER.start();

        ARestrictionManager.ITEM_INSTANCE.synchronizeWithClient(player);
        ARestrictionManager.RECIPE_INSTANCE.synchronizeWithClient(player);
        ARestrictionManager.MOB_INSTANCE.synchronizeWithClient(player);
        ARestrictionManager.ORE_INSTANCE.synchronizeWithClient(player);

        ModNetworking.sendTo(player, new RequestReloadS2CPacket(ReloadType.ITEM));
        ModNetworking.sendTo(player, new RequestReloadS2CPacket(ReloadType.RECIPE));
        ModNetworking.sendTo(player, new OreStagesSyncerS2CPacket(ARestrictionManager.ORE_STAGES.stream().toList()));
        ModNetworking.sendTo(player, new DimensionIdsSyncerS2CPacket(ARestrictionManager.DIMENSION_INSTANCE.getIds()));

        AStages.TIMER.stop();
        AStages.LOGGER.info("AStages synchronization took {}!", AStages.TIMER);
    }

    public static void reflectServerStagesChangesToClients(@Nullable ServerPlayer player, MinecraftServer server) {
        var data = ServerStageData.getData(server);
        ModNetworking.sendTo(player, new ServerStagesSyncerS2CPacket(data.get()));
    }

    public static void reloadAfterScripts() {
        ARestrictionManager.ITEM_INSTANCE.reloadAfterScripts();

        if (ServerLifecycleHooks.getCurrentServer() == null) { return; }
        clientSynchronization(null);
    }

    public static void clearClientOnLogin(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new RequestReloadS2CPacket(ReloadType.CLIENT_BEFORE));
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable T getRestrictionById(@NotNull ARestrictionType type, String id) {
        return switch (type) {
            case ITEM -> (T) ITEM_INSTANCE.getRestriction(id);
            case MOB -> (T) MOB_INSTANCE.getRestriction(id);
            case DIMENSION -> (T) DIMENSION_INSTANCE.getRestriction(id);
            case STRUCTURE -> (T) STRUCTURE_INSTANCE.getRestriction(id);
            case RECIPE -> (T) RECIPE_INSTANCE.getRestriction(id);
            case SCREEN -> (T) SCREEN_INSTANCE.getRestriction(id);
            case ORE -> (T) ORE_INSTANCE.getRestriction(id);
            case PET -> (T) PET_INSTANCE.getRestriction(id);
            case ENCHANT -> (T) ENCHANT_INSTANCE.getRestriction(id);
            case CROP -> (T) CROP_INSTANCE.getRestriction(id);
            case EFFECT -> (T) EFFECT_INSTANCE.getRestriction(id);
            case REGION -> (T) REGION_INSTANCE.getRestriction(id);
        };
    }
}
