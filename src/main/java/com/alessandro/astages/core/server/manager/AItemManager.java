package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.item.*;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.item.ItemModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.ItemPredicateSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.ItemSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.ItemTagSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestRestrictionDeleteS2CPacket;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.ClientSynchronizable;
import com.alessandro.astages.store.server.AMinimalManager;
import com.alessandro.astages.util.ARestrictionType;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public class AItemManager implements AMinimalManager<ABaseItemRestriction<?, ?>>, ClientSynchronizable {
    private final List<ABaseItemRestriction<?, ?>> restrictions = new ArrayList<>();
    private final Map<String, ABaseItemRestriction<?, ?>> IDS = new HashMap<>();

    private final List<AItemRestriction> items = new ArrayList<>();
    private final List<AItemModRestriction> mods = new ArrayList<>();
    private final List<AItemTagRestriction> tags = new ArrayList<>();
    private final List<AItemPredicateRestriction> predicates = new ArrayList<>();

    private final List<ABaseItemRestriction<?, ?>> INVENTORY_CACHE = new ArrayList<>();
    private final List<ABaseItemRestriction<?, ?>> EQUIPMENT_CACHE = new ArrayList<>();
    private final List<ABaseItemRestriction<?, ?>> CONTAINERS_CACHE = new ArrayList<>();

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

        INVENTORY_CACHE.clear();
        EQUIPMENT_CACHE.clear();
        CONTAINERS_CACHE.clear();
    }

    public void reloadAfterScripts() {
        restrictions.forEach(restriction -> {
            if (restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
                INVENTORY_CACHE.add(restriction);
            }

            if (restriction.isDisabled(Attributes.EQUIPPING)) {
                EQUIPMENT_CACHE.add(restriction);
            }

            if (restriction.isDisabled(Attributes.STORING_IN_CONTAINERS)) {
                CONTAINERS_CACHE.add(restriction);
            }
        });
    }

    public ABaseItemRestriction<?, ?> getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public ABaseItemRestriction<?, ?> getRestriction(Player player, ItemStack stack) {
        return restrictions.stream().filter(r -> r.isRestricted(stack) && !AStagesUtil.hasStage(player, r.getStage())).findFirst().orElse(null);
    }

    public List<ABaseItemRestriction<?,?>> getAllRestrictions(ItemStack stack) {
        return restrictions.stream().filter(r -> r.isRestricted(stack)).toList();
    }

    public ABaseItemRestriction<?, ?> getInventoryRestriction(Player player, ItemStack stack) {
        return INVENTORY_CACHE.stream().filter(r -> r.isRestricted(stack) && !AStagesUtil.hasStage(player, r.getStage())).findFirst().orElse(null);
    }

    public ABaseItemRestriction<?, ?> getEquipmentRestriction(Player player, ItemStack stack) {
        return EQUIPMENT_CACHE.stream().filter(r -> r.isRestricted(stack) && !AStagesUtil.hasStage(player, r.getStage())).findFirst().orElse(null);
    }

    public ABaseItemRestriction<?, ?> getContainersRestriction(Player player, ItemStack stack) {
        return CONTAINERS_CACHE.stream().filter(r -> r.isRestricted(stack) && !AStagesUtil.hasStage(player, r.getStage())).findFirst().orElse(null);
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

    private boolean commonAddOperations(ABaseItemRestriction<?, ?> restriction) {
        if (IDS.containsKey(restriction.getId())) {
            if (AStagesCommon.ENABLE_LOGS.get()) {
                AStages.LOGGER.warn("Restriction with id {} already found!", restriction.getId());
            }

            return false;
        }

        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);

        ARestrictionManager.ALL_IDS.add(restriction.getId());
        ARestrictionManager.ALL_STAGES.add(restriction.getStage());
        return true;
    }

    public void recalculateInventoryAndEquipment(ABaseItemRestriction<?, ?> restriction) {
        INVENTORY_CACHE.removeIf(r -> r.getId().equals(restriction.getId()));
        EQUIPMENT_CACHE.removeIf(r -> r.getId().equals(restriction.getId()));
        CONTAINERS_CACHE.removeIf(r -> r.getId().equals(restriction.getId()));

        if (restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
            INVENTORY_CACHE.add(restriction);
        }

        if (restriction.isDisabled(Attributes.EQUIPPING)) {
            EQUIPMENT_CACHE.add(restriction);
        }

        if (restriction.isDisabled(Attributes.STORING_IN_CONTAINERS)) {
            CONTAINERS_CACHE.add(restriction);
        }
    }

    public void removeRestriction(String id) {
        restrictions.removeIf(restriction -> restriction.getId().equals(id));
        items.removeIf(restriction -> restriction.getId().equals(id));
        mods.removeIf(restriction -> restriction.getId().equals(id));
        tags.removeIf(restriction -> restriction.getId().equals(id));
        predicates.removeIf(restriction -> restriction.getId().equals(id));
        INVENTORY_CACHE.removeIf(restriction -> restriction.getId().equals(id));
        EQUIPMENT_CACHE.removeIf(restriction -> restriction.getId().equals(id));
        CONTAINERS_CACHE.removeIf(restriction -> restriction.getId().equals(id));
        IDS.remove(id);

        ModNetworking.sendTo(null, new RequestRestrictionDeleteS2CPacket(id, associatedType()));
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        items.forEach(restriction -> ModNetworking.sendTo(player, new ItemSyncerS2CPacket(restriction)));
        tags.forEach(restriction -> ModNetworking.sendTo(player, new ItemTagSyncerS2CPacket(restriction)));
        mods.forEach(restriction -> ModNetworking.sendTo(player, new ItemModSyncerS2CPacket(restriction)));
        predicates.forEach(restriction -> ModNetworking.sendTo(player, new ItemPredicateSyncerS2CPacket(restriction)));
    }

    public ARestrictionType associatedType() {
        return ARestrictionType.ITEM;
    }
}
