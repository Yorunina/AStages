package com.alessandro.astages.core.client;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.networking.packet.syncer.IsItemRestrictedC2SPacket;
import com.alessandro.astages.util.AClientManager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// SOLVED ISSUE WITH RECIPE

@OnlyIn(Dist.CLIENT)
public class AClientItemManager implements AClientManager {
    public final Map<String, List<AClientItemRestriction>> restrictions = new HashMap<>();
    public final List<ItemStack> notRestricted = new ArrayList<>();
//    private final Map<ItemStack, AClientItemRestriction> CACHE = new HashMap<>();

    public void reloadBeforeScripts() {
        restrictions.clear();
        notRestricted.clear();
//        CACHE.clear();
    }

    public void addRestriction(String stage, AClientItemRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());
        // if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id(), restriction.id())); }
        newList.add(restriction);
        restrictions.put(stage, newList);

//        CACHE.put(restriction.stack(), restriction);
    }

    public void notRestricted(ItemStack stack) {
        notRestricted.add(stack);
    }

    public AClientItemRestriction getRestriction(@NotNull ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        for (var s : notRestricted) {
            if (ItemStack.matches(s, stack)) {
                return null;
            }
        }

        for (String stage : restrictions.keySet()) {
            for (AClientItemRestriction restriction : restrictions.get(stage)) {
                if (ItemStack.matches(restriction.stack(), stack)) {
                    if (ClientPlayerStage.hasStage(stage)) {
                        return null;
                    } else {
                        return restriction;
                    }
                }
            }
        }

        AStages.LOGGER.debug("REQUESTED FOR {}", stack);
        PacketDistributor.sendToServer(new IsItemRestrictedC2SPacket(stack));
        return null;
    }
}
