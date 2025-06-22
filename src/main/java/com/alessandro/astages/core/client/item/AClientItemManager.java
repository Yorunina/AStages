package com.alessandro.astages.core.client.item;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.integration.jei.CustomItemStackKey;
import com.alessandro.astages.networking.packet.item.RequestItemPropertyC2SPacket;
import com.alessandro.astages.store.client.AClientRestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AClientItemManager {
    private final List<AClientRestriction<?, ?, ItemStack>> restrictions = new ArrayList<>();
    private final HashMap<String, AClientRestriction<?, ?, ItemStack>> ids = new HashMap<>();
    private final List<AClientItemRestriction> items = new ArrayList<>();
    private final List<AClientTagRestriction> tags = new ArrayList<>();
    private final List<AClientModRestriction> mods = new ArrayList<>();
    private final List<AClientPredicateRestriction> predicates = new ArrayList<>();

    private final HashMap<CustomItemStackKey, AClientItemPropertyRestriction> properties = new HashMap<>();

    static {
        NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeStagesEvent.class, e -> {
            if (e.getOperation() != PlayerStage.Operation.GET) {
                AClientRestrictionManager.ITEM_INSTANCE.clearProperties();
            }
        });
    }

    public List<AClientItemRestriction> getItemRestrictions() {
        return items;
    }

    public List<AClientTagRestriction> getTagRestrictions() {
        return tags;
    }

    public List<AClientModRestriction> getModRestrictions() {
        return mods;
    }

    public List<AClientPredicateRestriction> getPredicates() {
        return predicates;
    }

    public List<AClientRestriction<?, ?, ItemStack>> getRestrictions() {
        return restrictions;
    }

    public void clearProperties() {
        properties.clear();
    }

    public void reloadBeforeScripts() {
        restrictions.clear();
        items.clear();
        tags.clear();
        mods.clear();
    }

    public void addRestriction(AClientItemRestriction restriction) {
        commonAddOperations(restriction);
        items.add(restriction);
    }

    public void addRestriction(AClientTagRestriction restriction) {
        commonAddOperations(restriction);
        tags.add(restriction);
    }

    public void addRestriction(AClientModRestriction restriction) {
        commonAddOperations(restriction);
        mods.add(restriction);
    }

    public void addRestriction(AClientPredicateRestriction restriction) {
        commonAddOperations(restriction);
        predicates.add(restriction);

    }

    public void addRestriction(AClientItemPropertyRestriction restriction) {
        properties.put(CustomItemStackKey.build(restriction.stack()), restriction);
    }

    private void commonAddOperations(AClientRestriction<?, ?, ItemStack> restriction) {
        restrictions.add(restriction);
        ids.put(restriction.getId(), restriction);
    }

    public String getRestrictionIdForStack(@NotNull ItemStack stack) {
        for (var restriction : restrictions) {
            if (restriction.isRestricted(stack)) {
                return restriction.getId();
            }
        }

        return null;
    }

    public AClientItemPropertyRestriction getRestriction(@NotNull ItemStack stack) {
        if (properties.containsKey(CustomItemStackKey.build(stack))) {
            var restriction = properties.get(CustomItemStackKey.build(stack));
            if (restriction != null) {
                return ClientPlayerStage.hasStage(restriction.stage()) ? null : restriction;
            } else {
                return null;
            }
        }

        var id = getRestrictionIdForStack(stack);
        if (id != null) {
//            AStages.LOGGER.debug("Requested for stack: {}, id: {}", stack, id);
            PacketDistributor.sendToServer(new RequestItemPropertyC2SPacket(id, ids.get(id).getStage(), stack));
        } else {
            properties.put(CustomItemStackKey.build(stack), null);
        }

        return null;
    }

    public Set<String> getStagesForStack(@NotNull ItemStack stack) {
        Set<String> toReturn = new HashSet<>();

        restrictions.forEach(restriction -> {
            if (restriction.isRestricted(stack) && restriction.isHideInJei()) { toReturn.add(restriction.getStage()); }
        });

        return toReturn;
    }

    public Set<String> getStagesForResourceLocation(ResourceLocation resourceLocation) {
        Set<String> toReturn = new HashSet<>();

        mods.forEach(restriction -> {
            if (Objects.equals(restriction.getModId(), resourceLocation.getNamespace()) && restriction.isHideInJei()) {
                toReturn.add(restriction.getStage());
            }
        });

        return toReturn;
    }
}
