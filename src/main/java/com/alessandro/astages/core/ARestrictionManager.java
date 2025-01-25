package com.alessandro.astages.core;

import com.alessandro.astages.core.manager.*;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.RequestReRenderingS2CPacket;
import com.alessandro.astages.networking.packet.syncer.*;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.util.ARestrictionType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;
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

    public static Set<String> ALL_STAGES = new HashSet<>();
    public static Set<String> ORE_STAGES = new HashSet<>();

    public static void reloadBeforeScripts() {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            return;
        }

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

        ModNetworking.sendToClients(new RequestClientReloadS2CPacket());
    }

    public static void reloadAfterScripts() {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            return;
        }

        // ITEMS AUTOMATICALLY -> question/answer system
        // JEI
        ModNetworking.sendToClients(new RequestJeiClientReloadS2CPacket());

        // RECIPE
        ARestrictionManager.RECIPE_INSTANCE.getRestrictions().forEach((s, r) -> r.forEach(restriction -> ModNetworking.sendToClients(new JeiRecipeSyncerS2CPacket(restriction.getId(), s, restriction.getPriority(), restriction.getType(), restriction.getRecipes()))));

        // ORE
        ARestrictionManager.ORE_INSTANCE.getRestrictions().forEach((s, r) -> r.forEach(restriction -> {
            ModNetworking.sendToClients(new OreSyncerS2CPacket(restriction.getId(), s, restriction.getOriginal(), restriction.getReplacement(), false));
        }));

        // MOB
        ARestrictionManager.MOB_INSTANCE.getRestrictions().forEach((s, r) -> r.forEach(restriction -> {
            ModNetworking.sendToClients(new MobSyncerS2CPacket(restriction.getId(), restriction.getStage(), restriction.getMobs(), restriction.get(Attributes.Mob.JADE_MOB_MESSAGE).get()));
        }));

        // ORE STAGES
        synchronizeOreStages(null);

        ModNetworking.sendToClients(new RequestReRenderingS2CPacket());
    }

    public static void synchronizeOreStages(ServerPlayer player) {
        if (player == null) {
            ModNetworking.sendToClients(new OreStagesSyncerS2CPacket(ARestrictionManager.ORE_STAGES.stream().toList()));
        } else {
            ModNetworking.sendToPlayer(new OreStagesSyncerS2CPacket(ARestrictionManager.ORE_STAGES.stream().toList()), player);
        }
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
        };
    }
}
