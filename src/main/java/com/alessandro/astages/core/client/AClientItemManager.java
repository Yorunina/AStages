package com.alessandro.astages.core.client;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.networking.ModNetworking;
import com.alessandro.astages.networking.packet.ud.IsItemRestrictedC2SPacket;
import com.alessandro.astages.util.AClientManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.*;

// SOLVED ISSUE WITH RECIPE

@OnlyIn(Dist.CLIENT)
public class AClientItemManager implements AClientManager {
    public final Map<String, List<AClientItemRestriction>> restrictions = new HashMap<>();
    // public Map<ItemStack, AClientItemRestriction> CACHE = new HashMap<>();

    public void reloadBeforeScripts() {
        restrictions.clear();
    }

    public void addRestriction(String stage, AClientItemRestriction restriction) {
        var newList = restrictions.getOrDefault(stage, new ArrayList<>());
        if (!newList.isEmpty()) { newList.removeIf(rest -> Objects.equals(rest.id(), restriction.id())); }
        newList.add(restriction);
        restrictions.put(stage, newList);
    }

    public AClientItemRestriction getRestriction(@NotNull ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        for (String stage : restrictions.keySet()) {
            for (AClientItemRestriction restriction : restrictions.get(stage)) {
                if (restriction.stack().equals(stack, false)) {
                    if (ClientPlayerStage.hasStage(stage)) {
                        return null;
                    } else {
                        return restriction;
                    }
                }
            }
        }

        ModNetworking.sendToServer(new IsItemRestrictedC2SPacket(stack));
        return null;
    }
}
