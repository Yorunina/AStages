package com.alessandro.astages.core.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.restriction.item.*;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.item.ItemSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.ModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.PredicateSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.TagSyncerS2CPacket;
import com.alessandro.astages.store.ClientSynchronizable;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AItemManager implements ClientSynchronizable {
    private final List<ABaseItemRestriction<?, ?>> restrictions = new ArrayList<>();
    private final Map<String, ABaseItemRestriction<?, ?>> IDS = new HashMap<>();

    private final List<AItemRestriction> items = new ArrayList<>();
    private final List<AItemModRestriction> mods = new ArrayList<>();
    private final List<AItemTagRestriction> tags = new ArrayList<>();
    private final List<AItemPredicateRestriction> predicates = new ArrayList<>();

    public List<AItemRestriction> getItemRestrictions() {
        return items;
    }

    public List<AItemModRestriction> getModRestrictions() {
        return mods;
    }

    public List<AItemTagRestriction> getTagRestrictions() {
        return tags;
    }

    public List<AItemPredicateRestriction> getPredicateRestrictions() {
        return predicates;
    }

    public List<ABaseItemRestriction<?, ?>> getRestrictions() {
        return restrictions;
    }

    public void reloadBeforeScripts() {
        restrictions.clear();
        IDS.clear();

        items.clear();
        mods.clear();
        tags.clear();
        predicates.clear();
    }

    public ABaseItemRestriction<?, ?> getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public ABaseItemRestriction<?, ?> getRestriction(Player player, ItemStack stack) {
        return restrictions.stream().filter(r -> r.isRestricted(stack) && !AStagesUtil.hasStage(player, r.getStage())).findFirst().orElse(null);
    }

    public void addRestriction(AItemRestriction restriction) {
        if (commonAddOperations(restriction)) {
            items.add(restriction);
        }
    }

    public void addRestriction(AItemTagRestriction restriction) {
        if (commonAddOperations(restriction)) {
            tags.add(restriction);
        }
    }

    public void addRestriction(AItemModRestriction restriction) {
        if (commonAddOperations(restriction)) {
            mods.add(restriction);
        }
    }

    public void addRestriction(AItemPredicateRestriction restriction) {
        if (commonAddOperations(restriction)) {
            predicates.add(restriction);
        }
    }

    private boolean commonAddOperations(@NotNull ABaseItemRestriction<?, ?> restriction) {
        if (IDS.containsKey(restriction.getId()) && AStagesCommon.ENABLE_LOGS.get()) {
            AStages.LOGGER.warn("Restriction with id {} already found!", restriction.getId());
            return false;
        }

        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);

        ARestrictionManager.ALL_STAGES.add(restriction.getStage());
        return true;
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        items.forEach(restriction -> ModNetworking.sendTo(player, new ItemSyncerS2CPacket(restriction)));
        tags.forEach(restriction -> ModNetworking.sendTo(player, new TagSyncerS2CPacket(restriction)));
        mods.forEach(restriction -> ModNetworking.sendTo(player, new ModSyncerS2CPacket(restriction)));
        predicates.forEach(restriction -> ModNetworking.sendTo(player, new PredicateSyncerS2CPacket(restriction)));
    }
}
